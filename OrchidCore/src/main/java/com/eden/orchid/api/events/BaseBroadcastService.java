package com.eden.orchid.api.events;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class BaseBroadcastService <T extends Annotation> {
    protected Map<String, Set<EdenPair<Method, Object>>> listeners;
    protected Stack<String> inProgress;

    public BaseBroadcastService() {
        this.listeners = new HashMap<>();
        this.inProgress = new Stack<>();
    }

    protected void findListeners(Object o, Class<T> annotationClass) {
        Arrays.stream(o.getClass().getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(annotationClass))
              .forEach(method -> {
                  T methodAnnotation = method.getAnnotation(annotationClass);

                  try {
                      String eventName = (String) methodAnnotation.getClass().getMethod("value").invoke(methodAnnotation);
                      if (EdenUtils.isEmpty(eventName)) {
                          throw new IllegalArgumentException("Annotation value must not be empty");
                      }
                      this.listeners.putIfAbsent(eventName, new HashSet<>());
                      this.listeners.get(eventName).add(new EdenPair<>(method, o));
                  }
                  catch (Exception e) {
                      throw new IllegalArgumentException("Annotation must contain a String value property");
                  }
              });
    }

    protected final void startBroadcast(String event) {
        for (String inProgress : inProgress) {
            if (event.equals(inProgress)) {
                throw new IllegalStateException(Clog.format("The event '#{$1}' is already in progress, it cannot be emitted again until this cycle has finished.", new Object[]{event}));
            }
        }

        inProgress.push(event);
    }

    protected final void endBroadcast() {
        inProgress.pop();
    }

    protected Object callMethod(Method method, Object target, Object... args) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        ArrayList<Object> params = new ArrayList<>();

        if (!EdenUtils.isEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params.add(args[i]);
                }
                else {
                    params.add(null);
                }
            }
        }

        //we are passing the same number of arguments as this method accepts. Check the types
        // for a type match
        boolean methodMatch = true;

        if (parameterTypes.length != params.size()) {
            if(parameterTypes.length == 1 && method.isVarArgs()) {
                methodMatch = true;
            }
            else {
                methodMatch = false;
            }
        }
        else {
            for (int i = 0; i < params.size(); i++) {

                //if the object passed in is null, we cannot determine if it matches the param type, but
                // we can just pass in the object at that index as a null object
                if (params.get(i) == null) {
                    continue;
                }

                // check for primitive types that have been boxed
                else if (parameterTypes[i].equals(byte.class) && params.get(i).getClass().equals(Byte.class)) {
                    continue;
                }
                else if (parameterTypes[i].equals(short.class) && params.get(i).getClass().equals(Short.class)) {
                    continue;
                }
                else if (parameterTypes[i].equals(int.class) && params.get(i).getClass().equals(Integer.class)) {
                    continue;
                }
                else if (parameterTypes[i].equals(long.class) && params.get(i).getClass().equals(Long.class)) {
                    continue;
                }
                else if (parameterTypes[i].equals(float.class) && params.get(i).getClass().equals(Float.class)) {
                    continue;
                }
                else if (parameterTypes[i].equals(double.class) && params.get(i).getClass().equals(Double.class)) {
                    continue;
                }
                else if (parameterTypes[i].equals(boolean.class) && params.get(i).getClass().equals(Boolean.class)) {
                    continue;
                }
                else if (parameterTypes[i].isAssignableFrom(params.get(i).getClass())) {
                    continue;
                }
                else {
                    methodMatch = false;
                    break;
                }
            }
        }

        Object[] objects = new Object[params.size()];

        for (int i = 0; i < params.size(); i++) {
            objects[i] = params.get(i);
        }

        // go ahead and try to fire the callback!
        if (parameterTypes.length == params.size() && methodMatch) {
            try {
                return method.invoke(target, objects);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (parameterTypes.length == (params.size() + 1) && method.isVarArgs()) {
            Object[] newObjects = Arrays.copyOf(objects, objects.length + 1);
            newObjects[newObjects.length - 1] = null;

            try {
                return method.invoke(target, newObjects);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(parameterTypes.length == 1 && method.isVarArgs()) {
            try {
                return method.invoke(target, new Object[] {objects});
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(method.isVarArgs()) {
            Object[] argArray = Arrays.copyOf(objects, parameterTypes.length);

            Object[] remainingArgs = new Object[objects.length - parameterTypes.length + 1];

            for (int i = 0; i < remainingArgs.length; i++) {
                remainingArgs[i] = objects[i + parameterTypes.length - 1];
            }

            argArray[argArray.length - 1] = remainingArgs;

            try {
                return method.invoke(target, argArray);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(parameterTypes.length == 0) {
            try {
                return method.invoke(target);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // otherwise, notify of the object types it expects
        else {
            List<String> types = Arrays.stream(objects).map(o -> (o != null) ? o.getClass().getName() : Object.class.getName()).collect(Collectors.toList());
            Clog.w("Cannot handle callback, because it should be able to handle the following parameters: (#{$1 | join(', ')})", types);
        }

        return null;
    }
}

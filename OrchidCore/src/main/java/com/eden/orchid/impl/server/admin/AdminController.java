package com.eden.orchid.impl.server.admin;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsDescription;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.server.OrchidController;
import com.eden.orchid.api.server.OrchidRequest;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.server.OrchidView;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.server.annotations.Get;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class AdminController extends OrchidController {

    private OrchidContext context;
    private Set<AdminList> adminLists;

    @Inject
    public AdminController(OrchidContext context, Set<AdminList> adminLists) {
        super(1000);
        this.context = context;
        this.adminLists = adminLists;
    }

    @Get(path = "/")
    public OrchidResponse index(OrchidRequest request) {
        OrchidView view = new OrchidView(context, this, "admin");

        return new OrchidResponse(context).view(view);
    }

    @Get(path = "/lists/:name/:id")
    public OrchidResponse renderListItem(OrchidRequest request, String name, String id) {
        AdminList foundList = null;
        for (AdminList list : adminLists) {
            if (list.getKey().equalsIgnoreCase(name)) {
                foundList = list;
                break;
            }
        }

        if (foundList != null) {
            Object listItem = foundList.getItem(id);
            if (listItem != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("adminList", foundList);
                data.put("listItem", listItem);

                OrchidView view = new OrchidView(context, this, "adminListItem", data);
                view.setTitle(id);
                view.setBreadcrumbs(new String[] {"lists", foundList.getKey()});

                return new OrchidResponse(context).view(view);
            }
        }

        return new OrchidResponse(context).status(404).content("List item not found");
    }

    @Get(path = "/withParams/:paramKey", params = AdminParams.class)
    public OrchidResponse testWithParams(OrchidRequest request, AdminParams params, String paramKey) {
        Map<String, Object> data = new HashMap<>();
        data.put("params", params);

        OrchidView view = new OrchidView(context, this, "adminParams", data);
        view.setTitle(paramKey);
        view.setBreadcrumbs(new String[] {"paramKey"});

        return new OrchidResponse(context).view(view);
    }

    public boolean hasOptions(Object object) {
        return object instanceof OptionsHolder;
    }

    public List<OptionsDescription> getOptions(Object object) {
        if(object instanceof OptionsHolder) {
            return ((OptionsHolder) object).describeOptions(context);
        }

        return new ArrayList<>();
    }

    public static class AdminParams implements OptionsHolder {

        @Option
        public String param1;

        @Option
        public int param2;

        @Option
        public String param3;

        @Option
        public String paramKey;

    }
}

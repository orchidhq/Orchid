package com.eden.orchid.impl.tasks;

import com.eden.common.util.EdenUtils;
import com.eden.krow.BorderSet;
import com.eden.krow.HorizontalAlignment;
import com.eden.krow.KrowTable;
import com.eden.krow.TableFormatter;
import com.eden.krow.borders.SingleBorder;
import com.eden.krow.formatters.AsciiTableFormatter;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.Set;

@Description("Print the Orchid help page.")
public final class HelpTask extends OrchidTask {

    private final Provider<OrchidContext> contextProvider;
    private final Provider<Set<OrchidTask>> tasks;

    @Inject
    public HelpTask(Provider<OrchidContext> contextProvider, Provider<Set<OrchidTask>> tasks) {
        super(10, "help", TaskService.TaskType.OTHER);
        this.contextProvider = contextProvider;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        TableFormatter<String> formatter = new AsciiTableFormatter(new SingleBorder());

        System.out.println(printHeader(formatter));
        System.out.println(printUsage(formatter));
        System.out.println(printTasks(formatter));
        System.out.println(printOptions(formatter));
    }

    private String printHeader(TableFormatter<String> formatter) {
        return "\n\nOrchid Static Site Generator.\nVersion " + contextProvider.get().getSite().getOrchidVersion() + "\n";
    }

    private String printUsage(TableFormatter<String> formatter) {
        KrowTable table = new KrowTable();
        table.setShowHeaders(false);
        table.setShowLeaders(false);

        table.cell("key", "header", cell -> {cell.setContent("Usage:"); cell.setHorizontalAlignment(HorizontalAlignment.LEFT); return null;});
        table.cell("key", "value", cell -> {cell.setContent("  (orchid) <" + String.join("> <", OrchidFlags.getInstance().getPositionalFlags()) + "> [--<flag> <flag value>]"); cell.setPaddingLeft(4); return null;});
        table.table(cell -> {
            cell.setWrapTextAt(80);
            return null;
        });
        return table.print(formatter);
    }

    private String printTasks(TableFormatter<String> formatter) {
        KrowTable table = new KrowTable();
        table.setShowHeaders(false);
        table.setShowLeaders(false);

        table.cell("key", "header", cell -> {cell.setContent("Tasks:"); return null;});
        for(OrchidTask task : tasks.get()) {
            table.cell("key", task.getName(), cell -> {cell.setContent(task.getName()); cell.setPaddingLeft(4); return null;});
            table.cell("description", task.getName(), cell -> {cell.setContent(task.getDescription()); return null;});
        }
        table.table(cell -> {
            cell.setWrapTextAt(80);
            return null;
        });
        return table.print(formatter);
    }

    private String printOptions(TableFormatter<String> formatter) {
        KrowTable table = new KrowTable();
        table.setShowHeaders(false);
        table.setShowLeaders(false);

        table.cell("key", "header", cell -> {cell.setContent("Options:"); return null;});
        for(OrchidFlag.FlagDescription flag : OrchidFlags.getInstance().describeFlags().values()) {
            table.cell("aliases", flag.getKey(), cell -> {
                if(!EdenUtils.isEmpty(flag.getAliases())) {
                    cell.setContent("-" + String.join(" -", flag.getAliases()));
                    cell.setPaddingLeft(4);
                }
                return null;
            });
            table.cell("key", flag.getKey(), cell -> {cell.setContent("--" + flag.getKey()); cell.setPaddingLeft(4); return null;});
            table.cell("description", flag.getKey(), cell -> {cell.setContent(flag.getDescription()); return null;});
        }
        table.table(cell -> {
            cell.setWrapTextAt(80);
            return null;
        });
        return table.print(formatter);
    }

    private class EmptyBorderSet implements BorderSet {

        @Override public char getB()  { return '\0'; }
        @Override public char getBl() { return '\0'; }
        @Override public char getBr() { return '\0'; }
        @Override public char getC()  { return '\0'; }
        @Override public char getCl() { return '\0'; }
        @Override public char getCr() { return '\0'; }
        @Override public char getH()  { return '\0'; }
        @Override public char getT()  { return '\0'; }
        @Override public char getTl() { return '\0'; }
        @Override public char getTr() { return '\0'; }
        @Override public char getV()  { return '\0'; }
        @Override public char getNl() { return '\n'; }

    }

}

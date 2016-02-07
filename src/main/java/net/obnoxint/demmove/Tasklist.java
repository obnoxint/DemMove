package net.obnoxint.demmove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * A Tasklist represents the output of the tasklist.exe command.
 * </p>
 * <p>
 * Each time an instance of this class is initialized, a call to {@link #TASKLIST_EXE} using the parameters
 * {@link #TASKLIST_ARGS} will be made by calling <i>Runtime.getRuntime().exec</i>.
 * </p>
 * <p>
 * This class can only be instantiated by using its static methods. All methods will throw a <i>RuntimeException</i> if
 * the initialization of the object fails.
 * </p>
 * <p>
 * A Tasklist contains elements of {@link RunningTask} and is not immutable. Modifying the list has no effect on the
 * system.
 * </p>
 */
public final class Tasklist extends ArrayList<RunningTask> {

    /**
     * <p>
     * Determines elements that should be removed from the resulting Tasklist.
     * </p>
     */
    public interface Filter {

        /**
         * <p>
         * Filters the given list.
         * </p>
         *
         * @param list
         *            the list to filter
         * @return the elements to remove
         */
        List<RunningTask> filter(List<RunningTask> list);

    }

    private static final long serialVersionUID = 1L;

    /**
     * Path to the tasklist executable.
     */
    public static final String TASKLIST_EXE = "\"C:\\Windows\\System32\\tasklist.exe\"";

    /**
     * Command arguments.
     */
    public static final String TASKLIST_ARGS = "/FO csv /NH"; // csv format without headers

    private static final String TASKLIST_CMD = TASKLIST_EXE + " " + TASKLIST_ARGS;

    /**
     * <p>
     * Creates a new Tasklist containing only those {@link RunningTask}s which match the given name.
     * </p>
     *
     * @param name
     *            the name
     * @return a Tasklist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static Tasklist byName(final String name) throws RuntimeException {
        return byName(name, true);
    }

    /**
     * <p>
     * Creates a new Tasklist containing only those {@link RunningTask}s which contain or match the given name.
     * </p>
     *
     * @param name
     *            the name
     * @param exact
     *            if <code>true</code>, the name must be equal to the name of the {@link RunningTask}
     * @return a Tasklist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static Tasklist byName(final String name, final boolean exact) throws RuntimeException {
        final Filter f = (l) -> {
            final List<RunningTask> r = new ArrayList<>();

            for (final RunningTask e : l) {
                if ((exact && !e.name.equals(name)) || (!exact && e.name.contains(name))) {
                    r.add(e);
                }
            }

            return r;
        };

        return getTasklist(f);
    }

    /**
     * <p>
     * Creates a new Tasklist containing a {@link RunningTask} with the given pid, provided it exists.
     * </p>
     * <p>
     * If the desired result is to retrieve the name of the task with the given pid, {@link #getTaskName(int)} may be
     * more convenient to use.
     * </p>
     *
     * @param pid
     *            the pid
     * @return a Tasklist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static Tasklist byPid(final int pid) throws RuntimeException {
        return byPid(pid, pid);
    }

    /**
     * <p>
     * Creates a new Tasklist containing only those {@link RunningTask}s which have a pid in the given range.
     * </p>
     *
     * @param min
     *            min pid
     * @param max
     *            max pid
     * @return a Tasklist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static Tasklist byPid(final int min, final int max) throws RuntimeException {
        final Filter f = (l) -> {
            final List<RunningTask> r = new ArrayList<>();

            for (final RunningTask e : l) {
                if (e.pid == null || e.pid < min || e.pid > max) {
                    r.add(e);
                }
            }

            return r;
        };

        return getTasklist(f);
    }

    /**
     * <p>
     * Creates a new Tasklist containing only those {@link RunningTask}s which were created by the provided session.
     * </p>
     *
     * @param session
     *            the name of the session
     * @return a Tasklist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static Tasklist bySession(final String session) throws RuntimeException {
        final Filter f = (l) -> {
            final List<RunningTask> r = new ArrayList<>();

            for (final RunningTask e : l) {
                if (!e.session.equals(session)) {
                    r.add(e);
                }
            }

            return r;
        };

        return getTasklist(f);
    }

    /**
     * <p>
     * Creates a new Tasklist containing only those {@link RunningTask}s which pass the given filters. <code>null</code>
     * may be passed instead of an empty list.
     * </p>
     * <p>
     * All other methods call through this method.
     * </p>
     *
     * @param filters
     *            the filters to apply
     * @return a Tasklist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static Tasklist getTasklist(final List<Filter> filters) throws RuntimeException {
        Tasklist r = null;
        try {
            r = new Tasklist(filters);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to retrieve tasklist.", e);
        }
        return r;
    }

    /**
     * @param pid
     *            the pid
     * @return the name of the task or <code>null</code> if the task with the given pid does not exist
     * @throws RuntimeException
     *             if the initialization of the object fails
     */
    public static String getTaskName(final int pid) throws RuntimeException {
        final Tasklist tl = byPid(pid);
        return tl.isEmpty() ? null : tl.get(0).name;
    }

    private static Tasklist getTasklist(final Filter filter) throws RuntimeException {
        final List<Filter> filters = new ArrayList<>();
        filters.add(filter);
        return getTasklist(filters);
    }

    private Tasklist(final List<Filter> filters) throws IOException {
        final Process p = Runtime.getRuntime().exec(TASKLIST_CMD);

        final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        final List<RunningTask> list = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {

            final String[] split = line.split(",");

            final String sname = split[0].replace("\"", "");
            final String spid = split[1].replace("\"", "");
            final String ssession = split[2].replace("\"", "");

            Integer pid = null;
            try {
                pid = Integer.parseInt(spid);
            } catch (final NumberFormatException e) {}

            list.add(new RunningTask(sname, pid, ssession));

        }

        if (filters != null) {
            final Set<RunningTask> filtered = new HashSet<>();
            for (final Filter f : filters) {
                filtered.addAll(f.filter(list));
            }
            list.removeAll(filtered);
        }

        addAll(list);

    }

    /**
     * <p>
     * Iterates over the collections and returns <code>true</code> if it contains a {@link RunningTask} matchig the
     * given pid.
     * </p>
     *
     * @param pid
     *            the pid
     * @return true if the collections contains the element
     */
    public boolean contains(final int pid) {
        for (final RunningTask e : this) {
            if (e.pid == pid) {
                return true;
            }
        }

        return false;
    }

}

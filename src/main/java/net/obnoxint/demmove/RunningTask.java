package net.obnoxint.demmove;

/**
 * <p>
 * Identifier for a task running in the operating system. Used as an element in a {@link Tasklist}.
 * </p>
 */
public final class RunningTask {

    final String name;

    final Integer pid;

    final String session;

    RunningTask(final String name, final Integer pid, final String session) {
        this.name = name;
        this.pid = pid;
        this.session = session;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RunningTask other = (RunningTask) obj;
        if (pid == null) {
            if (other.pid != null) {
                return false;
            }
        } else if (!pid.equals(other.pid)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public Integer getPid() {
        return pid;
    }

    public String getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pid == null) ? 0 : pid.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(name).append(" ")
                .append(pid).append(" ")
                .append(session).toString();
    }

}

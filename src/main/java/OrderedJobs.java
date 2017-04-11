import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrderedJobs {

    private List<Job> jobs = new ArrayList<>();

    public String sort() {
        StringBuilder builder = new StringBuilder();

        jobs.sort(new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                if (o1.dependsOn(o2)) {
                    return 1;
                } else if (o2.dependsOn(o1) ) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (Job job : jobs) {
            builder.append(job.getJobName());
        }
        return builder.toString();
    }

    public void register(char jobName) {
        Job job = fetchAndRegisterJob(jobName);
    }

    private Job fetchAndRegisterJob(char jobName) {
        if (!Character.isAlphabetic(jobName)) {
            throw new IllegalArgumentException();
        }

        Job job = new Job(jobName);
        Job foundJob = jobs.stream()
                .filter(job1 -> job1.equals(job))
                .findFirst()
                .orElse(job);

        if (!jobs.contains(foundJob)) {
            jobs.add(foundJob);
        }

        return foundJob;
    }

    public void register(char jobName, char dependencyName) {
        Job dependency = fetchAndRegisterJob(dependencyName);
        Job job = fetchAndRegisterJob(jobName);
        job.addDependency(dependency);

    }

    private class Job{

        public char getJobName() {
            return jobName;
        }

        private final char jobName;
        private final Set<Job> dependencies = new HashSet<>();

        public Job(char jobName) {
            this.jobName = jobName;
        }

        public void addDependency(Job dependency) {
            this.dependencies.add(dependency);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Job)) return false;
            Job job = (Job) o;
            return jobName == job.jobName;
        }

        @Override
        public int hashCode() {
            return Objects.hash(jobName);
        }

        public boolean dependsOn(Job dependency) {
            return dependencies.contains(dependency) ||
            dependencies.stream()
                    .anyMatch(d -> d.dependsOn(dependency));

        }
    }
}



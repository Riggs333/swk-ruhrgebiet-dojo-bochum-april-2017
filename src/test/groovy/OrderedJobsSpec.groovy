import spock.lang.Specification
import spock.lang.Unroll

class OrderedJobsSpec extends Specification {

    def "sorting no jobs returns empty string"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "sorting jobs"
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == ""
    }

    def "single jobs without dependencies returns this job"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "registering a single job"
        jobs.register('a' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "a"
    }

    def "single job with different name without dependencies returns this job"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "registering a single job"
        jobs.register('b' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "b"
    }

    def "two jobs with different names without dependencies returns both jobs"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "registering two jobs with different names"
        jobs.register('b' as char)
        jobs.register('c' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "bc" || sortedJobs == "cb"
    }

    @Unroll
    def "job with invalid name #illegalCharacter throws IllegalArgumentException"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "job with invalid name"
        jobs.register(illegalCharacter as char)

        then:
        thrown IllegalArgumentException

        where:
        illegalCharacter << ['#', '✨', '0', '9']
    }

    def "single jobs with single dependency returns this job after its dependency"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "registering a single job"
        jobs.register('a' as char, 'b' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "ba"
    }

    def "single job with single dependency and another job without dependency sort as expected"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "registering a single job"
        jobs.register('a' as char, 'b' as char)
        jobs.register('c' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs in ["bac", "cba", "bca"]
    }

    def "two jobs with transitive dependencies"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when:
        jobs.register('a' as char, 'b' as char)
        jobs.register('b' as char, 'c' as char)

        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "cba"
    }

    def "two jobs with transitive dependencies 2"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when:
        jobs.register('b' as char, 'c' as char)
        jobs.register('a' as char, 'b' as char)
        jobs.register('b' as char, 'c' as char)
        jobs.register('c' as char, 'd' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "dcba"
    }

    def "two independent job rows with transitive dependencies"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when:
        jobs.register('b' as char, 'a' as char)
        jobs.register('y' as char, 'x' as char)
        jobs.register('z' as char, 'y' as char)
        jobs.register('c' as char, 'b' as char)

        String sortedJobs = jobs.sort()

        then:
        sortedJobs in ["xyzabc", "abxyzc"]
    }

    //TODO check circle

    def "registering job multiple times returns single job"() {
        given: "ordered jobs"
        OrderedJobs jobs = new OrderedJobs()

        when: "registering a single job"
        jobs.register('a' as char)
        jobs.register('a' as char)
        jobs.register('a' as char)
        String sortedJobs = jobs.sort()

        then:
        sortedJobs == "a"
    }
}

import Organization.Companion.getOrg
import org.gradle.api.Project

data class Developer(val name: String, val url: String?, val email: String?, val org: Organization?) {

    companion object {
        fun Project.getDev(key: String): Developer {
            val name = property("${key}Name")?.toString() ?: error("Missing property ${key}Name")
            val url = property("${key}Url")?.toString()
            val email = property("${key}Email")?.toString()
            val orgKey = property("${key}Org")?.toString()
            val org = orgKey?.let { getOrg(it) }
            return Developer(name, url, email, org)
        }

        fun Project.getAllDevs(): Set<Developer> =
            properties.keys.asSequence()
                .filter { it.startsWith("developer") && it.endsWith("Name") }
                .map { it.replace("Name", "") }
                .distinct()
                .map { getDev(it) }
                .toSet()
    }
}

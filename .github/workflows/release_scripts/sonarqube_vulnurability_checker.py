import requests
import os
import textwrap

SONARQUBE_PROJECT_KEY = "SolaceLabs_event-management-agent"
SONARQUBE_PROJECT_MAIN_BRANCH = "main"
SONARQUBE_QUERY_TOKEN = os.getenv("SONARQUBE_QUERY_TOKEN")
SONARQUBE_HOTSPOTS_API_URL = os.getenv("SONARQUBE_HOTSPOTS_API_URL")
SONARQUBE_BLOCKING_VULNERABILITIES = "HIGH"
SONARQUBE_BLOCKING_HOTSPOT_STATUS = 'TO_REVIEW'


def block_print(long_string, each_line_length=75):
    print("\n\t".join(textwrap.wrap(long_string, each_line_length)))


def find_all_high_critical_vulnerabilities_to_resolve():
    sonarqube_hotspot_query_params = {
        "projectKey": SONARQUBE_PROJECT_KEY,
        "branch": SONARQUBE_PROJECT_MAIN_BRANCH,
        "status": SONARQUBE_BLOCKING_HOTSPOT_STATUS
    }

    hotspots_response = requests.get(
        SONARQUBE_HOTSPOTS_API_URL,
        params=sonarqube_hotspot_query_params,
        auth=(SONARQUBE_QUERY_TOKEN, '')).json()

    print(f"Total hotspots found for service: {SONARQUBE_PROJECT_KEY} is {hotspots_response['paging']['total']}")
    hotspots_to_resolve = dict()
    for hotspot in hotspots_response['hotspots']:
        if hotspot['vulnerabilityProbability'] in SONARQUBE_BLOCKING_VULNERABILITIES:
            hotspots_to_resolve[f"{hotspot['component']}:{hotspot['line']}"] = hotspot["message"]
    return hotspots_to_resolve


hotspots_to_resolve = find_all_high_critical_vulnerabilities_to_resolve()
if len(hotspots_to_resolve) != 0:
    print(f"❌ Following {SONARQUBE_BLOCKING_VULNERABILITIES} SonarQube hotspots should get resolved before release: ")
    for hotspot in hotspots_to_resolve:
        block_print(f"🔴️ {hotspot}")
        block_print(f'\t ➡️ {hotspots_to_resolve[hotspot]}\n')
        # print descriptions for this vulnerability
        # for vulnerability_description in prisma_vulnerabilities_to_resolve[vulnerability_name]:
        #    block_print(f'\t ➡️ {vulnerability_description}\n')
    exit(1)
else:
    print(f"No non-reviewed {SONARQUBE_BLOCKING_VULNERABILITIES} SonarQube hotspots found! ✅")

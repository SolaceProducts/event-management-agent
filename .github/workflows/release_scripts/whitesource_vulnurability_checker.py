import requests
import json
import os
import boto3

WS_REST_API_URL = 'https://saas.whitesourcesoftware.com/api'
WS_API_KEY = os.getenv('WS_APIKEY')
WS_PROJECT_TOKEN = os.getenv('WS_PROJECTTOKEN')
WS_BLOCKING_VULNERABILITIES = "critical,high"

def determine_any_exclusions():
    dynamodb_client = boto3.resource('dynamodb')

    whitesource_exclusion_table = dynamodb_client.Table('whitesource-excluded-libraries')
    whitesource_exclusion_entries = whitesource_exclusion_table.scan()['Items']

    exclusions = set()
    for exclusion in whitesource_exclusion_entries:
        exclusions.add(exclusion['library'])

    return exclusions


def find_all_high_critical_vulnerabilities_to_resolve(excluded_libraries):
    ws_payload = {
        'requestType': 'getProjectVulnerabilityReport',
        'format': 'json',
        'userKey': WS_API_KEY,
        'projectToken': WS_PROJECT_TOKEN
    }
    headers = {'content-type': 'application/json'}
    ws_vulnerability_report_response = requests.request(
        'post',
        WS_REST_API_URL,
        data=json.dumps(ws_payload),
        headers=headers)

    project_ws_vulnerabilities = json.loads(ws_vulnerability_report_response.text)
    vulnerabilities_to_resolve = dict()
    for vulnerability in project_ws_vulnerabilities['vulnerabilities']:
        if vulnerability['severity'] in WS_BLOCKING_VULNERABILITIES:
            library_full_name = f"{vulnerability['library']['artifactId']}-{vulnerability['library']['version']}.jar"
            if library_full_name in excluded_libraries:
                print(f"\nLibrary {library_full_name} has vulnerabilities but is in exclusion list ")
            else:
                vulnerabilities_to_resolve[vulnerability['name']] = \
                    f"{library_full_name} (Suggested fix: {vulnerability['topFix']['fixResolution']})"
    return vulnerabilities_to_resolve


whitesource_exclusion_list = determine_any_exclusions()
whitesource_vulnerabilities_to_resolve = find_all_high_critical_vulnerabilities_to_resolve(whitesource_exclusion_list)
if len(whitesource_vulnerabilities_to_resolve) != 0:
    print(f"❌ Following {WS_BLOCKING_VULNERABILITIES} whitesource vulnerabilities should get resolved before release:")
    for vulnerability_name in whitesource_vulnerabilities_to_resolve:
        print(f"- {vulnerability_name}: {whitesource_vulnerabilities_to_resolve[vulnerability_name]}")
    exit(1)
else:
    print("No {WS_BLOCKING_VULNERABILITIES} whitesource vulnerabilities found! ✅")

import requests
import json
import os

WS_REST_API_URL = 'https://saas.whitesourcesoftware.com/api'
WS_USER_KEY = os.getenv('WHITESOURCE_API_KEY')
WS_PROJECT_TOKEN = os.getenv('WHITESOURCE_PROJECT_TOKEN')


def find_all_high_critical_vulnerabilities():
    ws_payload = {
        'requestType': 'getProjectVulnerabilityReport',
        'format': 'json',
        'userKey': WS_USER_KEY,
        'projectToken': WS_PROJECT_TOKEN
    }
    headers = {'content-type': 'application/json'}
    ws_vulnerability_report_response = requests.request(
        'post',
        WS_REST_API_URL,
        data=json.dumps(ws_payload),
        headers=headers)

    project_ws_vulnerabilities = json.loads(ws_vulnerability_report_response.text)
    high_vulnerabilities = dict()
    for vulnerability in project_ws_vulnerabilities['vulnerabilities']:
        if ('high' or 'critical') in vulnerability['severity']:
            high_vulnerabilities[vulnerability['name']] = vulnerability['library']['artifactId'] + '-' + \
                                                          vulnerability['library']['version'] + '.jar'
    return high_vulnerabilities


print("Hi")

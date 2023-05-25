import requests
import json
import os
import boto3

WS_REST_API_URL = 'https://saas.whitesourcesoftware.com/api'
WS_API_KEY = os.getenv('WS_APIKEY')
WS_PROJECT_TOKEN = os.getenv('WS_PROJECTTOKEN')


def find_all_high_critical_vulnerabilities():
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
    high_vulnerabilities = dict()
    for vulnerability in project_ws_vulnerabilities['vulnerabilities']:
        if ('high' or 'critical') in vulnerability['severity']:
            high_vulnerabilities[vulnerability['name']] = vulnerability['library']['artifactId'] + '-' + \
                                                          vulnerability['library']['version'] + '.jar' + \
                                                          f" (Suggested fix: {vulnerability['topFix']['fixResolution']})"
    return high_vulnerabilities


a = find_all_high_critical_vulnerabilities()
print(a)

import requests
import json
import os

PRISMA_ROOT_API_URL = os.getenv("PRISMA_ROOT_API_URL")
PRISMA_AUTHENTICATE_URL = f"{PRISMA_ROOT_API_URL}/authenticate"
PRISMA_REGISTRY_SCAN_RESULT_URL = f"{PRISMA_ROOT_API_URL}/registry"
PRISMA_ACCESS_KEY = os.getenv('PRISMA_ACCESS_KEY')
PRISMA_ACCESS_KEY_SECRET = os.getenv('PRISMA_ACCESS_KEY_SECRET')
HTTP_JSON_HEADER = 'application/json'
# DOCKER_IMAGE_TO_CHECK is the repository name full and the tag that is used to scan it
DOCKER_IMAGE_TO_CHECK = os.getenv("DOCKER_IMAGE_TO_CHECK")
PRISMA_BLOCKING_VULNERABILITIES = "critical,high"


def find_all_high_critical_vulnerabilities_to_resolve():
    # Authenticate
    authenticate_payload = {
        'username': PRISMA_ACCESS_KEY,
        'password': PRISMA_ACCESS_KEY_SECRET
    }
    authenticate_headers = {'Accept': HTTP_JSON_HEADER, 'Content-type': HTTP_JSON_HEADER}

    get_token_request = requests.post(
        PRISMA_AUTHENTICATE_URL,
        data=json.dumps(authenticate_payload),
        headers=authenticate_headers
    )
    token_response = json.loads(get_token_request.text)
    scan_result_token = token_response['token']

    # Get scan results
    scan_result_header = {
        'accept': HTTP_JSON_HEADER,
        'content-type': HTTP_JSON_HEADER,
        'Authorization': f'Bearer {scan_result_token}'
    }
    prisma_scan_result_request = requests.get(
        f'{PRISMA_REGISTRY_SCAN_RESULT_URL}?name={DOCKER_IMAGE_TO_CHECK}',
        data=json.dumps(authenticate_payload),
        headers=scan_result_header
    )

    prisma_project_vulnerabilities = prisma_scan_result_request.json()[0].get('vulnerabilities')
    vulnerabilities_to_resolve = dict()
    for vulnerability in prisma_project_vulnerabilities:
        if vulnerability['severity'] in PRISMA_BLOCKING_VULNERABILITIES:
            package_full_name = f"{vulnerability['packageName']}-{vulnerability['packageVersion']}"
            vulnerability_description = vulnerability['description']
            if package_full_name not in vulnerabilities_to_resolve:
                vulnerabilities_to_resolve[package_full_name] = [vulnerability_description]
            else:
                vulnerabilities_to_resolve[package_full_name].append(vulnerability_description)
    return vulnerabilities_to_resolve


prisma_vulnerabilities_to_resolve = find_all_high_critical_vulnerabilities_to_resolve()
if len(prisma_vulnerabilities_to_resolve) != 0:
    print(f"Following {PRISMA_BLOCKING_VULNERABILITIES} Prisma vulnerabilities should get resolved before release: ❌ ")
    for vulnerability_name in prisma_vulnerabilities_to_resolve:
        print(f"- {vulnerability_name}")
    exit(1)
else:
    print(f"No {PRISMA_BLOCKING_VULNERABILITIES} Prisma vulnerabilities found! ✅")

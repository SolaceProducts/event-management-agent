echo "Preparing maven settings file"
envsubst < settings-template.xml > "local-settings.xml"
cp local-settings.xml ~/.m2/settings.xml

echo "Configuring ssh key"
mkdir -p ~/.ssh
echo "$SSH_PRIVATE_KEY" > ~/.ssh/id_rsa
chmod 600 ~/.ssh/id_rsa

echo "Setting StrictHostKeyChecking no"
echo -e "Host *\n\tStrictHostKeyChecking no\n\n" > ~/.ssh/config

echo "Whitelisting  github.com in ~/.ssh/known_hosts"
ssh-keyscan -t rsa github.com >> ~/.ssh/known_hosts

echo "Configuring Git User"
git config user.email "actions@github.com"
git config user.name "GitHub Actions"

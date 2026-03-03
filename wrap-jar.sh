#!/bin/bash

# Build script for expense-cli
# Builds JAR, copies to home directory, and creates wrapper script

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_NAME="expense-cli.jar"

echo "Building JAR file..."
cd "$PROJECT_DIR"
mvn package -q

echo "Copying JAR to ~/$JAR_NAME..."
cp target/agent-expense-tracker-1.0-SNAPSHOT.jar ~/"$JAR_NAME"

echo "Creating wrapper script at /usr/local/bin/expense..."
sudo tee /usr/local/bin/expense > /dev/null << 'EOF'
#!/bin/bash
java -jar ~/expense-cli.jar "$@"
EOF

echo "Making wrapper executable..."
sudo chmod +x /usr/local/bin/expense

echo "Done! You can now use 'expense' command from anywhere."
# zt-bind-converter
Converts all your zerotier networks to bind zone files

## Usage
1. Build the project:
	```
    mvn package
	```
2. Run the project with environment variables:
	```
    ZT_TOKEN="apitoken" MAIN_DOMAIN="example.com" PRIMARY_NS="ns.example.com"
	```

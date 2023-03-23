.PHONY: all

all: package deploy

deploy:
	@- echo "Deploying DayTrader locally"
	@- docker compose up --always-recreate-deps --force-recreate --build --detach

package: update web-service gateway-service accounts-service quotes-service portfolios-service

update:
	@- echo "Updating all submodules"
	@- git submodule update --recursive

web-service:
	@- echo "Building Web Service"
	@- cd ./daytrader-web-service && ./mvnw clean package

gateway-service:
	@- echo "Building Web Service"
	@- cd ./daytrader-gateway-service && ./mvnw clean package

accounts-service:
	@- echo "Building Web Service"
	@- cd ./daytrader-accounts-service && ./mvnw clean package

quotes-service:
	@- echo "Building Web Service"
	@- cd ./daytrader-quotes-service && ./mvnw clean package

portfolios-service:
	@- echo "Building Web Service"
	@- cd ./daytrader-portfolios-service && ./mvnw clean package

clean:
	@- echo "Cleaning all build artifacts"
	@- cd ./daytrader-web-service && ./mvnw clean
	@- cd ./daytrader-gateway-service && ./mvnw clean
	@- cd ./daytrader-accounts-service && ./mvnw clean
	@- cd ./daytrader-quotes-service && ./mvnw clean
	@- cd ./daytrader-portfolios-service && ./mvnw clean


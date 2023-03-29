local: package 

deploy:
	@- echo "Deploying DayTrader"
	@- docker compose up --always-recreate-deps --force-recreate --build --detach

package: web-service gateway-service accounts-service quotes-service portfolios-service

web-service:
	@- echo "Cloning and building locally, the latest copy of web Service."
	@- git clone --depth=1 git@github.com:sample-daytrader/daytrader-web-service
	@- cd ./daytrader-web-service && ./mvnw clean package spring-boot:run &

gateway-service:
	@- echo "Cloning and building locally, the latest copy of gateway Service."
	@- git clone --depth=1 git@github.com:sample-daytrader/daytrader-gateway-service
	@- cd ./daytrader-gateway-service && ./mvnw clean package spring-boot:run &

accounts-service:
	@- echo "Cloning and building locally, the latest copy of accounts Service."
	@- git clone --depth=1 git@github.com:sample-daytrader/daytrader-accounts-service
	@- cd ./daytrader-accounts-service && ./mvnw clean package spring-boot:run &

quotes-service:
	@- echo "Cloning and building locally, the latest copy of quotes Service."
	@- git clone --depth=1 git@github.com:sample-daytrader/daytrader-quotes-service
	@- cd ./daytrader-quotes-service && ./mvnw clean package spring-boot:run &

portfolios-service:
	@- echo "Cloning and building locally, the latest copy of portfolios Service."
	@- git clone --depth=1 git@github.com:sample-daytrader/daytrader-portfolios-service
	@- cd ./daytrader-portfolios-service && ./mvnw clean package spring-boot:run &

clean:
	@echo "Terminating applications running on ports 1443 to 5443"
	@-lsof -ti :1443 | xargs kill
	@-lsof -ti :2443 | xargs kill
	@-lsof -ti :3443 | xargs kill
	@-lsof -ti :4443 | xargs kill
	@-lsof -ti :5443 | xargs kill	
	@- echo "Cleaning all build artifacts"
	@- rm -rf ./daytrader-web-service
	@- rm -rf ./daytrader-gateway-service
	@- rm -rf ./daytrader-accounts-service
	@- rm -rf ./daytrader-quotes-service
	@- rm -rf ./daytrader-portfolios-service


# Daytrader Microservices


### Prequisites

The only pre-requisite is `Docker`.


### Build instructions

1. Clone this repo (including all its submodules):
```sh
git clone https://github.com/sample-daytrader/sample.daytrader.microservices.git
```

2. From the root folder, run:
```sh
make deploy 
```

3. Now, just go to localhost:5443 to start using the application.

### Development Instructions

In development mode, we'll make a local clone of all the participating services and deploy the local container images of 
these services.

You can run `make local` to pull all the individual services, package them with maven, build a local docker image out of
them and deploy them.


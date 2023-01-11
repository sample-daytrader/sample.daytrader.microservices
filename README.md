### Build instructions

1. From the root folder, run:
```
 ./mvnw clean package
```

2. To deploy, from the root folder, run:

```
docker compose up --always-recreate-deps --force-recreate --build --detach
```

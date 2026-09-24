.PHONY: run test build clean

run:
	./mvnw spring-boot:run

test:
	./mvnw test

build:
	./mvnw clean package

clean:
	./mvnw clean

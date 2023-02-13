clean-build:
	mvn clean package

run:
	java -jar target/kp.jar

build-run: clean-build run

docker-build: clean-build
	sudo docker build -t kprime-cli .

docker-run:
	sudo docker run -it --rm \
                -v /home/nipe/.kprime/:/kprime-cli/  \
                -e "KPRIME_HOME=/kprime-cli/" \
                --network=host \
                -p 7000:7000 \
                kprime-cli

# make docker-deploy -e "kpver=beta16"
docker-deploy: docker-build
	sudo docker tag kprime-cli nipedot/kprime-cli:$(kpver)
	sudo docker push nipedot/kprime-cli:$(kpver)

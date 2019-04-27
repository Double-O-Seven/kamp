FROM i386/ubuntu

RUN apt-get update && \
	apt-get install -y openjdk-8-jdk-headless gcc g++ cmake git python-pip && \
	pip install ply

RUN cd /tmp && \
	git clone --depth 1 https://github.com/Zeex/samp-plugin-sdk.git samp-plugin-sdk && \
	git clone --depth 1 https://github.com/Zeex/sampgdk.git sampgdk && \
	cd sampgdk && \
	mkdir build && \
	cd build && \
	cmake .. -DSAMP_SDK_ROOT=/tmp/samp-plugin-sdk -DSAMPGDK_STATIC=OFF -DSAMPGDK_BUILD_PLUGINS=OFF -DSAMPGDK_BUILD_DOCS=OFF && \
	cmake --build . --config Release && \
	cmake --build . --config Release --target install && \
	cd / && \
	rm -rf /tmp/samp-plugin-sdk && \
	rm -rf /tmp/sampgdk

RUN useradd --uid 1000 --user-group --create-home kamp && \
	mkdir /home/kamp/build

USER kamp

WORKDIR /home/kamp/build

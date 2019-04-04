FROM ubuntu:16.04
RUN apt-get update
RUN apt-get -qqy install wget xvfb

ARG CHROME_VERSION="google-chrome-stable"
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update -qqy
RUN apt-get -qqy install ${CHROME_VERSION:-google-chrome-stable}
RUN rm /etc/apt/sources.list.d/google-chrome.list \
RUN rm -rf /var/lib/apt/lists/* /var/cache/apt/*

# Add Chrome as a user
RUN groupadd -r chrome && useradd -r -g chrome -G audio,video chrome \
    && mkdir -p /home/chrome && chown -R chrome:chrome /home/chrome \
		&& mkdir -p /opt/google/chrome && chown -R chrome:chrome /opt/google/chrome

ADD src /usr/src/app/src
ADD pom.xml /usr/src/app/pom.xml

RUN cd /usr/src/app

RUN chmod 777 src/test/java/my/selenium/drivers/chromedriver
RUN chmod 777 src/test/java/my/selenium/drivers/geckodriver

# Run Chrome non-privileged
USER chrome

ENTRYPOINT ["/bin/bash", "-c", "whereis google-chrome"]

# ENTRYPOINT ["google-chrome", "--no-sandbox", "--headless", "--disable-gpu"]
FROM node:24-alpine

# Download and install Hurl
RUN wget https://github.com/Orange-OpenSource/hurl/releases/download/4.3.0/hurl-4.3.0-x86_64-unknown-linux-gnu.tar.gz && \
    tar -xzf hurl-4.3.0-x86_64-unknown-linux-gnu.tar.gz && \
    mv hurl-4.3.0-x86_64-unknown-linux-gnu/bin/hurl /usr/local/bin/hurl && \
    chmod +x /usr/local/bin/hurl && \
    rm -rf hurl-4.3.0-x86_64-unknown-linux-gnu.tar.gz hurl-4.3.0-x86_64-unknown-linux-gnu

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .
CMD ["npm", "start"]
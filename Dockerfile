# Use a base image that has GraalVM installed
FROM oracle/graalvm-ce:latest as graalvm

# Set the working directory inside the container
WORKDIR /app

# Copy the source code and any necessary files to the container
COPY . /app

# Set the necessary environment variables
ENV LANG C.UTF-8
ENV GRAALVM_HOME /opt/graalvm

# Install native-image component
RUN gu install native-image

# Build the native image
RUN native-image -jar your-application.jar --no-server

# Use a minimal base image for the final container
FROM debian:latest

# Copy the native image from the previous stage
COPY --from=graalvm /app/webfux-security /app/webfux-security

# Set the working directory inside the final container
WORKDIR /app

# Expose any necessary ports
EXPOSE 9090

# Set the command to run the native image
CMD ["./webfux-security"]

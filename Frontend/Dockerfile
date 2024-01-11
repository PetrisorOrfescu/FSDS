# Use an official Node.js runtime as a parent image
FROM node:16-alpine
# Set the working directory inside the container
WORKDIR /app
# Copy package.json and package-lock.json (if available) to the container
COPY package*.json ./
# Install application dependencies
RUN npm install --force
# Copy the rest of the application code to the container
COPY . .
# Build the React application
RUN npm run build
# Define the command to start your application
CMD ["npm", "start"]
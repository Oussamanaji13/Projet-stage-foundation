# Development server for Angular application
FROM node:20-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Expose port for development server
EXPOSE 4200

# Health check
HEALTHCHECK --interval=15s --timeout=5s --start-period=20s --retries=10 \
  CMD curl -fsS http://localhost:4200/ || exit 1

# Start development server
CMD ["npm", "run", "start", "--", "--host", "0.0.0.0", "--port", "4200"]

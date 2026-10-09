# build
FROM maven:3.8.8-amazoncorretto-21-al2023 as build

# Criando pasta chamada build
WORKDIR /build

# Comando para copiar os arquivos do diretório para dentro da pasta build. Recebe dois parâmetros (origem e destino)
COPY . .

# Comando para executar algo dentro do contêiner
RUN mvn clean package -DskipTests

# run
FROM amazoncorretto:21.0.5
WORKDIR /app

COPY --from=build ./build/target/*.jar ./libraryapi.jar

# Comando para sinalizar qual porta a aplicação vai escutar as conexões
EXPOSE 8080
EXPOSE 9090

# Referência das variáveis de ambiente dentro do contêiner
ENV DATABASE_URL=''
ENV DATABASE_USERNAME=''
ENV DATABASE_PASSWORD=''
ENV GOOGLE_CLIENT_ID='client_id'
ENV GOOGLE_CLIENT_SECRET='client_id'

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

# Comando para rodar e inicializar a aplicação
ENTRYPOINT java -jar libraryapi.jar
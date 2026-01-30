🎟️ Event Ticket Service (AWS Infrastructure Challenge)
Este projeto é um simulador de venda de ingressos de alta performance, desenvolvido para validar conceitos de escalabilidade, alta disponibilidade e infraestrutura como código (IaC).

O objetivo principal não é a complexidade do código da aplicação em si, mas sim a robustez da infraestrutura na AWS para suportar picos massivos de tráfego — como a abertura de vendas para um show do Coldplay ou Queen.

🚀 O Desafio
Como garantir que um sistema simples não saia do ar quando milhares de usuários tentam acessar o mesmo recurso simultaneamente? A resposta está na "brincadeira" feita na Cloud.

Diferenciais do Projeto:
Infrastructure as Code: Toda a infraestrutura foi provisionada utilizando Terraform.

Elasticidade: Implementação de Auto Scaling e Load Balancing para lidar com tráfego variável.

Resiliência de Dados: Cluster de banco de dados com instâncias de leitura (Read Replicas) para desafogar o Master.

Performance: Uso de Redis para caching estratégico.

🏗️ Arquitetura do Sistema
Abaixo, apresento a evolução do pensamento arquitetural do projeto:

1. Estado Atual vs. Objetivo Final
   Na esquerda, o sistema em sua fase inicial; na direita, a topografia final planejada para suportar a escala de estádio.

(Caminho sugerido: salve sua imagem na pasta assets e renomeie aqui)

2. Fluxo de Dados na VPC
   O diagrama abaixo detalha como as requisições fluem do Load Balancer para as instâncias EC2 e como a separação de Write (Master) e Read (Slaves/Replicas) funciona no banco de dados.

🛠️ Tecnologias Utilizadas
Linguagem: Java / Spring Boot (Backend simplificado).

Infraestrutura: AWS (EC2, RDS, ELB, VPC, ASG).

Provisionamento: Terraform.

Cache: Redis.

Banco de Dados: PostgreSQL/MySQL em Cluster.

📈 Aprendizados
A utilização do Terraform foi o ponto de virada neste projeto. Apesar da curva de aprendizado inicial com a documentação, a produtividade ganha ao tratar infraestrutura como software é imbatível. O projeto prova que, mesmo sem uma arquitetura complexa de microserviços ou brokers, uma Cloud bem configurada resolve a maioria dos problemas de escala de uma aplicação monolítica bem escrita.

# Iniciar o Terraform
terraform init

# Validar o plano de execução
terraform plan

# Aplicar a infra na AWS
terraform apply
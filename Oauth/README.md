# Recursos principais do Keycloak

1. **Autenticação e Autorização**
   - Oferece várias opções de autenticação (senha, OTP, redes sociais como Google e Facebook, etc.).
   - Autorização baseada em permissões de usuários e grupos (Role-Based Access Control - RBAC).
   
2. **Single Sign-On (SSO)**
   - Usuários podem acessar várias aplicações com um único login.
   
3. **Administração Centralizada**
   - O Keycloak possui uma interface administrativa que facilita a gestão de usuários, permissões e integrações.

4. **Autenticação Social**
   - Integração com provedores de identidade externos, como Google, GitHub, Facebook, entre outros.

5. **Multitenancy e Realms**
   - Keycloak é **multi-tenant**, o que significa que você pode ter diferentes "realms". Cada **realm** pode ser entendido como uma instância separada, com sua própria configuração de usuários, grupos, e autenticação.

6. **Token-Based Authentication**
   - Usa **JSON Web Tokens (JWT)** para autenticar usuários em aplicações e APIs.

## Estrutura básica do Keycloak

- **Realms**: Uma coleção de usuários, roles (permissões), e configurações. Cada realm é isolado dos outros e pode ter seu próprio conjunto de aplicações e usuários.
- **Clients**: São as aplicações que usam o Keycloak para autenticação, como uma aplicação web, mobile, ou API.
- **Roles**: Representam as permissões. Elas podem ser associadas a usuários ou grupos para controle de acesso baseado em funções.
- **Users**: Representa os usuários no sistema, que podem ser gerenciados diretamente no Keycloak ou através de um provedor externo (LDAP, Google, etc.).
- **Groups**: Conjuntos de usuários que podem compartilhar as mesmas permissões e roles.

## Como o fluxo de autenticação funciona?

1. **Login Redirecionado**: Quando um usuário tenta acessar uma aplicação protegida por Keycloak, ele é redirecionado para a página de login do Keycloak.
2. **Autenticação no Keycloak**: O Keycloak lida com a autenticação e verifica as credenciais do usuário.
3. **Token de Acesso**: Após a autenticação bem-sucedida, o Keycloak gera um **Access Token** (geralmente um JWT) e o envia de volta para a aplicação.
4. **Autorização**: A aplicação usa o token recebido para garantir que o usuário tenha permissão para acessar os recursos solicitados.
5. **Renovação de Token**: O token de acesso tem uma duração limitada. O Keycloak também emite um **Refresh Token** para permitir que o usuário obtenha novos tokens de acesso sem precisar fazer login novamente.

## Como começar a usar o Keycloak?

**Configuração Inicial**:
   - Após a instalação, você pode acessar o console administrativo (`http://localhost:8080/admin/`) e logar com as credenciais do administrador.
   - Crie um **realm** para separar seu ambiente de autenticação (por exemplo, para desenvolvimento ou produção).
   - Registre um **client** que será sua aplicação, como uma API ou aplicação web.
   

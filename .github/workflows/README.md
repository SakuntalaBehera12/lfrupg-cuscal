### GitHub Actions Workflows

This project includes the following workflows:

- [Build and Deploy](.github/workflows/main.yml): This workflow is triggered on any push to the `main` branch. It includes security scans, code quality checks, and packaging of the application. After these checks, it deploys the application to the development environment.

- [Scan, Build and Package Application](.github/workflows/wf-build.yml): This workflow is responsible for preparing the application for deployment. It includes security scans, code quality checks, building the application, running unit tests, and creating an artifact of the build.

- [Deploy](.github/workflows/wf-deploy.yml): This workflow is triggered by a `workflow_call`. It deploys the artifact to the specified environment and optionally runs integration tests. The environment, whether to run integration tests, and the test suite to run can be specified as inputs when triggering the workflow.

- [Build and Deploy Manually](.github/workflows/manual-deployment-trigger.yml): This workflow can be triggered manually from the GitHub Actions UI. It allows you to choose whether to execute the deployment, run integration tests, and select the target environment for deployment.

- [Publish Release](.github/workflows/wf-publish-release.yml): This workflow is triggered when a new release is created. It handles the publishing of the release to the specified environments.

- [Push Release](.github/workflows/wf-push-release.yml): This workflow is triggered manually and handles the release deployment across different environments.

For more details about each workflow, please refer to the [Grouped Workflows](#grouped-workflows) section in the workflows' README.

## Grouped Workflows

1. CI
2. CD 
3. Publish Release
4. Push Release 
5. Manual Deployment Trigger



```bash
Build and Deploy (main.yml)
│  
├── CI (wf-build.yml)
│   ├── Security Scans
│   │   ├── Security, Secrets and Dependency Scans
│   │   └── Run SAST and SCA Scans
│   ├── Code Quality Scans
│   │   └── Code Quality Check
│   └── Build and Publish Artifact
│       ├── Build Application
│       ├── Run Unit Tests
│       ├── Run Code Coverage Check
│       └── Publish Artifact
│
├── CD (Deploy to Dev)
│   └── Deploy (wf-deploy.yml)
│       ├── Deploy Application
│       ├── Run Post-Deployment Tests
│       └── Upload Test Results


Publish Release (wf-publish-release.yml)
    ├── Generate Release Tag
    └── Create Release


Push Release (wf-push-release.yml)
    ├── Deploy Release to TEST (Conditional)
    ├── Deploy Release to UAT (Conditional)
    ├── Deploy Release to PreProd (Conditional)
    └── Deploy Release to Prod (Conditional)


Build and Deploy Manually (manual-deployment-trigger.yml)
│
├── Build (Use wf-build.yml)
│   ├── Security Check
│   └── Code Quality Check
│   └── Build and Publish Artifact
│       ├── Build Application
│       ├── Run Unit Tests
│       ├── Run Code Coverage Check
│       └── Publish Artifact
│
└── Deploy to Environment (Use wf-deploy.yml)
    ├── Deploy to Specified Environment
    ├── Run Post-Deployment Tests (Optional)
    └── Run Test Suite

```
### Considerations

1. **Separation of 'Artifact' and 'Release' Deployment Workflows**  
   Maintain separate workflows for artifact deployment and release deployment. This approach enhances clarity and ensures that only production-ready versions are deployed to your live environment.

   - **Deployment of Artifacts:**
     - **Environments:**
       - Dev
       - SIT/Test
       - UAT/QA

   - **Deployment of Releases:**
     - **Environments:**
       - Preprod/Staging (if you have it)
       - Production

### CI/CD Pipeline Overview

The `main.yml` workflow is triggered automatically on any push to or pull request involving any branch in the repository, ensuring the CI/CD pipeline runs for all changes across all branches.

It internally calls the reusable build `wf-build.yml`and deploy `wf-deploy.yml` workflows to automate the CI/CD pipeline, ensuring that code is thoroughly checked and securely deployed to the development environment.

- **Build and Package (wf-build.yml):**  
  This job is triggered when there is a push to the `main` branch. is responsible for preparing the application for deployment. It includes security scans, such as secret scanning and dependency scanning, as well as Static Application Security Testing (SAST) and Software Composition Analysis (SCA). It also checks the quality of the code, including code coverage and linting. After these checks, it builds the application, runs unit and optional integration tests, and creates an artifact of the build. This artifact is then uploaded for future use.

- **Deploy to Dev (wf-deploy.yml):**
  After the build job completes successfully, the next workflow, "Deploy to Dev", handles the deployment of the built application to a development environment. It starts by downloading the previously created artifacts and preparing the deployment files. After the deployment, it performs post-deployment verification. It also includes a testing phase (wf-test/action.yml), where it sets up the environment, installs dependencies, runs functional and optional non-functional tests, and uploads the test results. After all these steps, it performs cleanup.

- **create-release (wf-create-release.yml)**: Manages the release creation process. It sets the necessary permissions, provides the workflow-run ID, generates a release tag, and pushes the release to GitHub.

- **publish-release (wf-publish-release.yml)**: Handles the release deployment across different environments. It fetches the release, then sequentially deploys it to Test, UAT, Preprod, and Prod environments by invoking the reusable `wf-deploy.yml` workflow.

- **Build and Deploy Manually (manual-deployment-trigger.yml):**  
  This workflow is triggered manually through the GitHub Actions UI. It allows users to select whether to execute the deployment, run post-deployment tests, choose the target environment for deployment, and decide whether to run security and code quality checks. The workflow consists of two jobs:
    - **Build:** This job uses the `wf-build.yml` workflow to build the application and run security and code quality checks if requested.
    - **Deploy to environment-name:** This job is dependent on the successful completion of the Build job. It uses the `wf-deploy.yml` workflow to deploy the application to the specified environment if the deployment was requested and the workflow was not triggered by Dependabot. It also decides which test suite to run based on the branch that triggered the workflow.



## Liferay Build and Deployment

This section describes the build and deployment process for the Liferay portal application, including the configuration structure and environment setup.

### Project Structure

The project follows a standard Liferay workspace structure with additional configuration directories:

```
├── configs/
│   ├── common/            # Common configuration files shared across all environments
│   │   └── deploy/        # Common deployable artifacts
│   ├── docker/            # Docker-related configurations
│   ├── liferay/           # Liferay-specific configurations
│   │   └── sdlc/          # Software Development Lifecycle configurations
│   │       └── deploy/    # Deployment scripts
│   └── .env.<environment>/# Environment-specific configurations
│       ├── config.properties  # Environment properties file
│       └── db.properties      # Generated database properties file (during deployment)
├── fragments/             # Liferay fragments directory
├── modules/               # Liferay modules directory
├── themes/                # Liferay themes directory
└── resources/             # Additional resources
```

### Configuration Structure

#### Environment Configuration Files

The project uses environment-specific configuration files located in `.env.<environment>` folders where `<environment>` can be:
- `dev` - Development environment
- `test` - Test environment
- `uat1`, `uat2` - User Acceptance Testing environments
- `prod1`, `prod2` - Production environments
- `dr1`, `dr2` - Disaster Recovery environments
- `local` - Local development environment

#### config.properties

The `config.properties` file in each environment folder contains crucial configuration parameters that are used during the deployment process. Key properties include:

| Property | Description |
|----------|-------------|
| `classname` | JDBC driver class name for database connection |
| `db_url` | Database connection URL |
| `db_username` | Database username |
| `liferay_server_connection` | The server connection string for deployment |

Example `config.properties` file:
```properties
classname=oracle.jdbc.OracleDriver
db_url=jdbc:oracle:thin:@//dbserver:1521/LIFERAY
db_username=liferay_user
liferay_server_connection=liferay-app-server.domain.com
```

#### db.properties

During the deployment process, a `db.properties` file is generated from the values in `config.properties` and the DB password stored in GitHub Secrets. This file is used by Liferay for database connectivity.

Example generated `db.properties`:
```properties
jdbc.default.driverClassName=oracle.jdbc.OracleDriver
jdbc.default.url=jdbc:oracle:thin:@//dbserver:1521/LIFERAY
jdbc.default.username=liferay_user
jdbc.default.password=********
```

### Build Process

The build process uses Gradle to compile, test, and package the Liferay application. Key build commands include:

- `./gradlew clean build "-Pliferay.workspace.environment=common"` - Basic build command
- `./gradlew distBundleZip` - Creates a distributable Liferay bundle

During the build process:
1. Fragments are automatically packaged into zip files and copied to the deployment directory
2. Configuration files are prepared for the target environment
3. The application is packaged into a deployable bundle

### Deployment Process

The deployment process is handled by GitHub Actions workflows and follows these steps:

1. **Artifact Download**: The build artifact is downloaded from previous workflow runs
2. **Configuration Setup**: Environment-specific configuration files are prepared
   - Values are read from `config.properties` in the appropriate `.env.<environment>` folder
   - A `db.properties` file is generated with database connection details
3. **Deployment**: 
   - Files are transferred to the target server using SFTP
   - Deployment scripts are executed on the remote server
   - Post-deployment tests are optionally executed to verify the deployment


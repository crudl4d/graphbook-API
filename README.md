<a name="readme-top"></a>
<!-- PROJECT LOGO -->
<br />
<div align="center">

<h3 align="center">Dogebook API</h3>

  <p align="center">
    This is documentation for Dogebook API project
    <br />
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This is an API for a social service called Dogebook.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

- Java 17
- Spring
- Maven
- Neo4j
- Github Actions

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started
### Prerequisites

You need to install java 17 and Maven
### Installation

1 Clone the repo
   ```sh
   git clone https://github.com/crudl4d/dogebook-API.git
   ```
2 Install the project, either with maven or IDE
   ```sh
   mvn clean install
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Following endpoints are currently available:

   ```sh
   /users GET
   /users/search GET
   /users/login POST
   /users/register POST
   /users/{userId} GET
   /users/{userId} PATCH
   /users/{userId}/profile-picture GET
   ```

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>
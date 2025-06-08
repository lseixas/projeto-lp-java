terraform {

  cloud {

    organization = "ProjetoLP"

    workspaces {
      name = "workspace-github-projeto-lp"
    }
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.11.0"
}

provider "aws" {
  region = "sa-east-1"
}

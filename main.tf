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

data "aws_ami" "latest_amazon_linux" {
    most_recent = true
    owners = ["amazon"]

    filter {
        name = "name"
        values = ["amzn2-ami-hvm-*-x86_64-gp2"]
    }

      filter {
        name   = "virtualization-type"
        values = ["hvm"]
    }
}

provider "aws" {
  region     = var.aws_region
  access_key = var.aws_access_key
  secret_key = var.aws_secret_key
}


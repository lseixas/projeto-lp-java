# variables.tf

variable "aws_access_key" {
  description = "AWS access key ID."
  type        = string
  sensitive   = true # This prevents Terraform from showing the value in logs
}

variable "aws_secret_key" {
  description = "AWS secret access key."
  type        = string
  sensitive   = true # This prevents Terraform from showing the value in logs
}

variable "aws_region" {
  description = "The AWS region to deploy resources in."
  type        = string
  default     = "sa-east-1" # Or your preferred default region
}
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
    region = "ap-northeast-2"
    # access_key = "환경 변수로 설정"
    # secret_key = "환경 변수로 설정"
}

data "aws_availability_zones" "available" {
  state = "available"
}

resource "aws_vpc" "JanghakRun" {
    cidr_block = "10.0.0.0/16"
    tags = {
        Name = "JanghakRun"
    }
}

resource "aws_subnet" "proxySubnet" {
  vpc_id     = aws_vpc.JanghakRun.id
  cidr_block = "10.0.1.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "proxySubnet"
  }
}

resource "aws_subnet" "wasSubnet" {
  vpc_id     = aws_vpc.JanghakRun.id
  cidr_block = "10.0.2.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "wasSubnet"
  }
}

resource "aws_subnet" "dbSubnet" {
  vpc_id     = aws_vpc.JanghakRun.id
  cidr_block = "10.0.3.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "dbSubnet"
  }
}
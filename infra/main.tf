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
  vpc_id            = aws_vpc.JanghakRun.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "proxySubnet"
  }
}

resource "aws_subnet" "wasSubnet" {
  vpc_id            = aws_vpc.JanghakRun.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "wasSubnet"
  }
}

resource "aws_subnet" "dbSubnet" {
  vpc_id            = aws_vpc.JanghakRun.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = data.aws_availability_zones.available.names[0]

  tags = {
    Name = "dbSubnet"
  }
}

resource "aws_internet_gateway" "internetGateway" {
  vpc_id = aws_vpc.JanghakRun.id

  tags = {
    Name = "JanghakRun_IGW"
  }
}

resource "aws_route_table" "publicRouteTable" {
  vpc_id = aws_vpc.JanghakRun.id

  tags = {
    Name = "JanghakRun_public_RT"
  }
}

resource "aws_route" "defaultRoute" {
  route_table_id         = aws_route_table.publicRouteTable.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.internetGateway.id
}

resource "aws_route_table_association" "proxySubnetAssociation" {
  subnet_id      = aws_subnet.proxySubnet.id
  route_table_id = aws_route_table.publicRouteTable.id
}

resource "aws_route_table" "privateRouteTable" {
  vpc_id = aws_vpc.JanghakRun.id

  tags = {
    Name = "JanghakRun_private_RT"
  }
}

resource "aws_route_table_association" "wasSubnetAssociation" {
  subnet_id      = aws_subnet.wasSubnet.id
  route_table_id = aws_route_table.privateRouteTable.id
}

resource "aws_security_group" "publicSg" {
  name        = "public-sg"
  description = "Allow all inbound traffic for public subnet"
  vpc_id      = aws_vpc.JanghakRun.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"          # All protocols
    cidr_blocks = ["0.0.0.0/0"] # All IP addresses
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  tags = {
    Name = "JanghakRun_Public_SG"
  }
}

resource "aws_security_group" "privateSg" {
  name        = "private-sg"
  description = "Allow HTTP, HTTPS, SSH from publicSg for private subnet"
  vpc_id      = aws_vpc.JanghakRun.id

  ingress {
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.publicSg.id]
  }

  ingress {
    from_port       = 443
    to_port         = 443
    protocol        = "tcp"
    security_groups = [aws_security_group.publicSg.id]
  }

  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.publicSg.id]
  }

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "JanghakRun_Private_SG"
  }
}

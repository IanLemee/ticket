terraform {
    required_providers{
        aws = {
            source = "hashicorp/aws"
            version = "~> 6.0"
        }
    }
}

provider "aws" {
    region = "us-east-1"
}

data "aws_ami" "ubuntu" {
    most_recent = true

    filter {
        name = "name"
        values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
    }

    filter {
        name = "virtualization-type"
        values = ["hvm"]
    }

    owners = ["099720109477"]
}

resource "aws_vpc" "ticket_vpc_lb" {
    cidr_block = "10.0.0.0/16"
}

resource "aws_internet_gateway" "gw" {
    vpc_id = aws_vpc.ticket_vpc_lb.id
}

resource "aws_route_table" "route_table" {
    vpc_id = aws_vpc.ticket_vpc_lb.id

    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.gw.id
    }
}

resource "aws_route_table_association" "a" {
    route_table_id = aws_route_table.route_table.id
    subnet_id = aws_subnet.ticket_subnet_lb_one.id
}

resource "aws_route_table_association" "b" {
    route_table_id = aws_route_table.route_table.id
    subnet_id = aws_subnet.ticket_subnet_lb_two.id
}

resource "aws_route_table_association" "c" {
    route_table_id = aws_route_table.route_table.id
    subnet_id = aws_subnet.ticket_subnet.id
}

resource "aws_subnet" "ticket_subnet_lb_one" {
    vpc_id = aws_vpc.ticket_vpc_lb.id
    cidr_block = "10.0.1.0/24"
    availability_zone = "us-east-1a"

}

resource "aws_subnet" "ticket_subnet_lb_two" {
    vpc_id = aws_vpc.ticket_vpc_lb.id
    cidr_block = "10.0.2.0/24"
    availability_zone = "us-east-1b"
}

resource "aws_subnet" "ticket_subnet" {
    vpc_id = aws_vpc.ticket_vpc_lb.id
    cidr_block = "10.0.3.0/24"
    availability_zone = "us-east-1c"
}

resource "aws_security_group" "aws_lb_sg" {
    vpc_id = aws_vpc.ticket_vpc_lb.id

    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }
}

resource "aws_lb" "load_balancer_ticket" {
    name = "ticketLb"
    internal = false
    load_balancer_type = "application"
    security_groups = [aws_security_group.aws_lb_sg.id]
    subnets = [aws_subnet.ticket_subnet_lb_one.id, aws_subnet.ticket_subnet_lb_two.id]

    enable_deletion_protection = false

}

resource "aws_lb_target_group" "lb_target_group" {
    name = "ticket-tg-v2"
    port = 80
    protocol = "HTTP"
    vpc_id = aws_vpc.ticket_vpc_lb.id

    lifecycle {
        create_before_destroy = true
    }

    health_check {
        path                = "/"
        protocol            = "HTTP"
        matcher             = "200"
        interval            = 30
        timeout             = 5
        healthy_threshold   = 2
        unhealthy_threshold = 2
    }
}

resource "aws_lb_listener" "lb_listener" {
    load_balancer_arn = aws_lb.load_balancer_ticket.arn
    port              = "80"
    protocol          = "HTTP"

    default_action {
        target_group_arn = aws_lb_target_group.lb_target_group.arn
        type             = "forward"
    }
}

resource "aws_instance" "ticket_concert" {
    ami = data.aws_ami.ubuntu.image_id
    instance_type = "t3.micro"
    associate_public_ip_address = true

    tags = {
        Name = "ticket_concert"
    }

    vpc_security_group_ids = [aws_security_group.ticket_sg.id]
    subnet_id = aws_subnet.ticket_subnet.id

    user_data = file("init-script.sh")
}

resource "aws_launch_template" "launch_template" {
    name_prefix = "launch_template_ticket"
    image_id = data.aws_ami.ubuntu.image_id
    instance_type = aws_instance.ticket_concert.instance_type
    user_data = filebase64("init-script.sh")

    network_interfaces {
        associate_public_ip_address = true

        security_groups = aws_instance.ticket_concert.vpc_security_group_ids
    }
    tag_specifications {
        resource_type = "instance"
        tags          = aws_instance.ticket_concert.tags
    }
    tag_specifications {
        resource_type = "volume"
        tags          = aws_instance.ticket_concert.tags
    }
}

resource "aws_autoscaling_group" "ticket_autoscaling" {
    vpc_zone_identifier = [aws_subnet.ticket_subnet_lb_one.id, aws_subnet.ticket_subnet_lb_two.id]
    desired_capacity = 2
    max_size = 5
    min_size = 2
    launch_template {
        id = aws_launch_template.launch_template.id
        version = aws_launch_template.launch_template.latest_version
    }
    target_group_arns = [aws_lb_target_group.lb_target_group.arn]
}

resource "aws_security_group" "ticket_sg" {
    name = "ticket_sg"
    vpc_id = aws_vpc.ticket_vpc_lb.id


    ingress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }
}

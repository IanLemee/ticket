output "lb_endpoint" {
  value = aws_lb.load_balancer_ticket.dns_name
}
terraform {
  required_providers {
    solacebroker = {
      source = "registry.terraform.io/solaceproducts/solacebrokerz"
    }
  }
}

provider "solacebroker" {
  alias    = "fh71c08said"
  username = "${var.username}"
  password = "${var.password}"
  url      = "${var.url}"
}

data "solacebroker_msg_vpn" "fh71c08said" {
  provider     = solacebroker.fh71c08said
  msg_vpn_name = "default"
}

resource "solacebroker_msg_vpn_queue" "fh71c08said-Consumer1" {
  provider                                      = solacebroker.fh71c08said
  msg_vpn_name                                  = data.solacebroker_msg_vpn.fh71c08said.msg_vpn_name
  owner                                         = "discovery"
  max_ttl                                       = 0
  queue_name                                    = "Consumer1"
  access_type                                   = "exclusive"
  max_msg_size                                  = 10000000
  permission                                    = "consume"
  dead_msg_queue                                = "#DEAD_MSG_QUEUE"
  max_bind_count                                = 1000
  delivery_delay                                = 0
  egress_enabled                                = true
  ingress_enabled                               = true
  max_msg_spool_usage                           = 5000
  redelivery_enabled                            = true
  respect_ttl_enabled                           = false
  max_redelivery_count                          = 0
  delivery_count_enabled                        = false
  event_bind_count_threshold                    = { "setPercent" : 80, "clearPercent" : 60 }
  reject_low_priority_msg_limit                 = 0
  respect_msg_priority_enabled                  = false
  event_msg_spool_usage_threshold               = { "setPercent" : 25, "clearPercent" : 18 }
  reject_low_priority_msg_enabled               = false
  consumer_ack_propagation_enabled              = true
  max_delivered_unacked_msgs_per_flow           = 10000
  reject_msg_to_sender_on_discard_behavior      = "when-queue-enabled"
  event_reject_low_priority_msg_limit_threshold = { "setPercent" : 80, "clearPercent" : 60 }
}

resource "solacebroker_msg_vpn_queue_subscription" "fh71c08said-Consumer1_a_b_c__" {
  provider           = solacebroker.fh71c08said
  msg_vpn_name       = solacebroker_msg_vpn_queue.fh71c08said-Consumer1.msg_vpn_name
  queue_name         = solacebroker_msg_vpn_queue.fh71c08said-Consumer1.queue_name
  subscription_topic = "a/b/c/>"
}

variable username {
}

variable password {
}

variable url {
}
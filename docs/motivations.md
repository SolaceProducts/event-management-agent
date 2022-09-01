# Motivations

As with any software system, event driven architectures (EDAs) evolve over time. There is great utility from
architectural, operational, and security perspectives in identifying the events being used in an EDA and how they are
being used. Having such knowledge allows an architect to plan the future evolution of the EDA. From an operations
perspective it allows the event infrastructure team to better understand current and future infrastructure requirements.
From an auditing and security perspective, it allows the identification of unexpected or even malicious uses of an eventing
system.

In chapter 3 of the
book [Flow Architectures](https://learning.oreilly.com/library/view/flow-architectures/9781492075882/), @jamesurquhart
uses a Wardley Map to describe the evolution of various flow integration components, and places "discovery" in the
"Genesis" phase.
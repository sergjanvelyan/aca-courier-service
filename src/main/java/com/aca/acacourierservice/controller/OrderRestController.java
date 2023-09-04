package com.aca.acacourierservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order")
public class OrderRestController {
    //  /create POST (ALL)

    //  /{orderId} GET (Courier, ADMIN, SToreAdmin )

    //  /list/unassigned GET (Courier, admin)

    //  /list GET (admin)

    //  /list/mine GET (Courier)

    //  /updateStatus/ PUT {Courier, Admin}

    //  /{orderId}/assignCourier PUT (Admin)

    //  /{orderId}/assignToMe PUT (courier)

}

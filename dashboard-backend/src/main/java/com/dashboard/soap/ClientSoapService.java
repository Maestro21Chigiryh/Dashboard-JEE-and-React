package com.dashboard.soap;

import java.util.List;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.jws.soap.SOAPBinding.Use;

@WebService(name = "ClientService", targetNamespace = "http://soap.dashboard.com/")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface ClientSoapService {
    
    @WebMethod(operationName = "getAllClients")
    @WebResult(name = "clients")
    List<ClientDTO> getAllClients();
    
    @WebMethod(operationName = "getTopClients")
    @WebResult(name = "topClients")
    List<ClientDTO> getTopClients(@WebParam(name = "limit") int limit);
    
    @WebMethod(operationName = "getClientCount")
    @WebResult(name = "count")
    long getClientCount();
    
    @WebMethod(operationName = "generateClients")
    @WebResult(name = "generationResult")
    GenerationResult generateClients(@WebParam(name = "count") int count);
    
    @WebMethod(operationName = "resetClients")
    @WebResult(name = "resetResult")
    ResetResult resetClients();
}
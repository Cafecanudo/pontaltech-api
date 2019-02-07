# API PontalTech

#### Descrição
> API para chamadas a serviços de envio de SMS da empresa [PontalTech](https://docs.pontaltech.com.br/pointer-sms-api) SMS.

## Unidades de Test
Cobertura de código:
<br/>
![](src/test/resources/cobertura_2.PNG)
<br/>
<br/>
![](src/test/resources/cobertura.PNG)

Tests implementados
<br/>
![](src/test/resources/test.PNG)

## Como usar
Configurações
    
    //Dentro do arquivos de profile tem as configurações de URL, usuário e senha, mas é possível
    //informar usuário e senha em Runtime.
      src/main/filters/dev.properties
      src/main/filters/prod.properties

  Criando resquisição
    
    //Sem DDD
    SMSRequest requisicao = SMSRequest.builder()
        .parar("6296521489")
        .parar("6296521482")
        .parar("6296521483")
        .mensagem("Mensagem")
        .codigoInterno("3245234")    //Id interno da Callink
        .build();
        
  Enviado SMS
  
    List<SMSResponse> resps = PontalTechAPI.preparar(requisicao).enviar();
    //Retorna uma lista dos SMSs enviados para cada numero adicionado

  Verificando status de Envio. *(Somente por período)*
      
      SMSRequestCheck requestCheck = SMSRequestCheck.builder()
              .dataInicio(LocalDateTime.now().minusMinutes(5))
              .dataFim(LocalDateTime.now())
              .build();
              
      List<SMSResponse> reports = PontalTechAPI.preparar(requestCheck)
              .check();        
    
  Espeficicando usuário e senha
  
    PontalTechAPI.config(
              SMSUserConfig.build("USUARIO", "SENHA")
            )
            .prepare(req)
            .envia();
    
#### Dúvidas 
[by Wellton S. Barros - makotostudiodev@gmail.com](https://github.com/Cafecanudo/zenvia)
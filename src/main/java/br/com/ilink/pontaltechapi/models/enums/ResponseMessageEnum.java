package br.com.ilink.pontaltechapi.models.enums;

public enum ResponseMessageEnum {
  COD_0(0, "Mensagem aceita. Aguardando agendamento."),
  COD_1(1, "Mensagem Agendada."),
  COD_2(2, "Mensagem enviada para operadora."),
  COD_3(3, "Confirmação de envio por parte da operadora."),
  COD_4(4, "Erro no envio por parte da operadora."),
  COD_5(5, "Confirmação de entrega por parte da operadora."),
  COD_6(6, "Erro na entrega por parte da operadora."),
  COD_7(7, "Mensagem bloqueada."),
  COD_8(8, "Mensagem invalida."),
  COD_9(9, "Mensagem cancelada."),
  COD_10(10, "Erro."),
  COD_11(11, "Sem crédito."),
  COD_12(12, "Mensagem não entregue."),
  COD_13(13, "Expirado"),
  COD_14(14, "Mensagem bloqueada na base de inválidos."),
  COD_100(100, "Numero inválido."),
  COD_101(101, "Numero de 11 dígitos inválido."),
  COD_102(102, "Numero de 10 dígitos inválido."),
  COD_103(103, "DDD inválido."),
  COD_104(104, "Mensagem tem mais de 160 caracteres (No caso do envio short)."),
  COD_105(105, "Mensagem vazia."),
  COD_106(106, "Conteúdo do objeto não é um array valido (Envio Multiplo)."),
  COD_107(107, "Mensagem vazia."),
  COD_108(108, "Referêcia tem mais de 50 caracteres."),
  COD_109(109, "Data de agendamento inválida."),
  COD_110(110, "Data inválida."),
  COD_111(111, "Período invalido."),
  COD_112(112, "Identificador inválido."),
  COD_113(113, "Url Inválido."),
  COD_114(114, "Conta sem crédito suficiente."),
  COD_115(115, "Conta inválida."),
  COD_116(116, "Referência inválida."),
  COD_1000(1000, "Não foi possivel registrar a requisição."),
  COD_1001(1001, "Não foi possivel criar a mensagem."),
  COD_1002(1002, "Não foi possivel retornar a mensagem."),
  COD_1003(1003, "Não foi possivel criar a mensagem."),
  COD_1004(1004, "Não foi possivel criar as mensagens."),
  COD_1005(1005, "Não foi possivel retornar as mensagens."),
  COD_1006(1006, "Não foi possivel criar as mensagens."),
  COD_1007(1007, "Não foi possivel retornar as respostas deste periodo."),
  COD_1008(1008, "Não foi possivel retornar a resposta."),
  COD_1009(1009, "Não foi possivel retornar a resposta."),
  COD_1010(1010, "Não foi possivel retornar multiplas respostas."),
  COD_1011(1011, "Requisição inválida."),
  COD_1012(1012, "Não foi possivel cancelar a mensagem.");

  private Integer codigo;
  private String mensagem;

  ResponseMessageEnum(Integer codigo, String mensagem) {
    this.codigo = codigo;
    this.mensagem = mensagem;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public String getMensagem() {
    return mensagem;
  }
}

package domain.enums;

public enum TamanioMascotaEnum {
  CHICO("Chico"),
  MEDIANO("Mediano"),
  GRANDE("Grande");

  private final String valor;

  TamanioMascotaEnum(String valor) {
    this.valor = valor;
  }

  public String getValor() {
    return valor;
  }

  @Override
  public String toString() {
        return valor;
  }
}
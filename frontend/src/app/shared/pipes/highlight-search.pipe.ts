import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Pipe({
  name: 'highlightSearch'
})
export class HighlightSearchPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer) { }

  /**
   * Sustituye las letras acentuadas con sus equivalentes sin acento en una cadena.
   * @param {string} texto - La cadena de texto con letras acentuadas.
   * @returns {string} - La cadena de texto sin letras acentuadas.
   */
  quitarTildes(texto: string): string {
    const mapaTildes: Record<string, string> = {
      'á': 'a', 'é': 'e', 'í': 'i', 'ó': 'o', 'ú': 'u',
      'Á': 'A', 'É': 'E', 'Í': 'I', 'Ó': 'O', 'Ú': 'U'
    };

    return texto?.replace(/[áéíóúÁÉÍÓÚ]/g, (match) => {
      return mapaTildes[match as keyof typeof mapaTildes] || match;
    });
  }


  transform(value: any, args: any): any {
    if(value ==null || value =='' || !value) return value;
    if (args && args !== null && args !== '' && value && value !== null && value !== '' ) {
      value = String(value); //Nos aseguramos de que sea un string
      let valueSinTildes = this.quitarTildes(value);
      let argsSinTildes = this.quitarTildes(args);
      const regex = new RegExp(argsSinTildes, 'gi');
      const match = valueSinTildes.match(regex);
      if (match !== null) {
        const highlightedValue = valueSinTildes.replace(regex, `<span class='highlight'>${match[0]}</span>`);
        return this.sanitizer.bypassSecurityTrustHtml(highlightedValue);
      }
    }
    return this.sanitizer.bypassSecurityTrustHtml(value);
  }
}

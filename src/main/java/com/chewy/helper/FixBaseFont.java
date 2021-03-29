package com.chewy.helper;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfName;

public abstract class FixBaseFont extends BaseFont {
  public static void fixBuiltinFonts() {

    if (BuiltinFonts14.size() != 14) {
      BuiltinFonts14.clear();

      BuiltinFonts14.put(COURIER, PdfName.COURIER);
      BuiltinFonts14.put(COURIER_BOLD, PdfName.COURIER_BOLD);
      BuiltinFonts14.put(COURIER_BOLDOBLIQUE, PdfName.COURIER_BOLDOBLIQUE);
      BuiltinFonts14.put(COURIER_OBLIQUE, PdfName.COURIER_OBLIQUE);
      BuiltinFonts14.put(HELVETICA, PdfName.HELVETICA);
      BuiltinFonts14.put(HELVETICA_BOLD, PdfName.HELVETICA_BOLD);
      BuiltinFonts14.put(HELVETICA_BOLDOBLIQUE, PdfName.HELVETICA_BOLDOBLIQUE);
      BuiltinFonts14.put(HELVETICA_OBLIQUE, PdfName.HELVETICA_OBLIQUE);
      BuiltinFonts14.put(SYMBOL, PdfName.SYMBOL);
      BuiltinFonts14.put(TIMES_ROMAN, PdfName.TIMES_ROMAN);
      BuiltinFonts14.put(TIMES_BOLD, PdfName.TIMES_BOLD);
      BuiltinFonts14.put(TIMES_BOLDITALIC, PdfName.TIMES_BOLDITALIC);
      BuiltinFonts14.put(TIMES_ITALIC, PdfName.TIMES_ITALIC);
      BuiltinFonts14.put(ZAPFDINGBATS, PdfName.ZAPFDINGBATS);
    }
  }
}

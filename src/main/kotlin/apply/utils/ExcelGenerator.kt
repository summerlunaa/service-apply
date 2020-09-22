package apply.utils

import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ExcelGenerator {
    companion object {
        private const val HEADER_ROW = 0

        fun generateBy(headerTitles: Array<String>, rows: List<ExcelRow>): ByteArrayInputStream {
            val workbook = SXSSFWorkbook()
            val sheet = workbook.createSheet()

            styleHeaders(workbook, sheet, headerTitles)
            fillData(sheet, rows, headerTitles.size)

            val out = ByteArrayOutputStream()
            workbook.write(out)
            workbook.close()

            return ByteArrayInputStream(out.toByteArray())
        }

        private fun styleHeaders(workbook: SXSSFWorkbook, sheet: SXSSFSheet, headerTitles: Array<String>) {
            val headerFont = workbook.createFont()
            headerFont.bold = true

            val headerCellStyle = workbook.createCellStyle()
            headerCellStyle.setFont(headerFont)
            headerCellStyle.fillBackgroundColor = IndexedColors.GREY_80_PERCENT.index

            val headerRow = sheet.createRow(HEADER_ROW)

            for (col in headerTitles.indices) {
                val cell = headerRow.createCell(col)
                cell.setCellValue(headerTitles[col])
                cell.cellStyle = headerCellStyle
            }
        }

        private fun fillData(sheet: SXSSFSheet, rows: List<ExcelRow>, columnSize: Int) {
            sheet.trackAllColumnsForAutoSizing()

            rows.forEachIndexed { index, it ->
                val row = sheet.createRow(index + 1)
                val properties = it.getData()
                properties.forEachIndexed { propertyIndex, property ->
                    row.createCell(propertyIndex).setCellValue(property.toString())
                }
            }

            for (col in 0 until columnSize) {
                sheet.autoSizeColumn(col)
            }
        }
    }
}
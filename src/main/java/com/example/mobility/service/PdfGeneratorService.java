/**
 * 
 */
package com.example.mobility.service;

import com.example.mobility.model.Employee;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfGeneratorService {

	public byte[] generateEmployeePdf(List<Employee> employees) throws DocumentException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);

		document.open();

		// Add title
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
		Paragraph title = new Paragraph("Customer Order Details", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20f);
		document.add(title);

		// Create table with 8 columns
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		// Table headers
		String[] headers = { "CustomerID", "First Name", "Last Name", "Email", "Salary", "Created Date",
				"Updated Date", "Actions" };

		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
			cell.setBackgroundColor(new BaseColor(33, 150, 243)); // Blue header
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
		}

		// Table data
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
		for (Employee employee : employees) {
			table.addCell(createCell(String.valueOf(employee.getId()), cellFont));
			table.addCell(createCell(employee.getFirstName(), cellFont));
			table.addCell(createCell(employee.getLastName(), cellFont));
			table.addCell(createCell(employee.getEmailId(), cellFont));
			table.addCell(createCell("$" + employee.getSalary(), cellFont));
			table.addCell(createCell(String.valueOf(employee.getCreatedDate()), cellFont));
			table.addCell(createCell(String.valueOf(employee.getUpdatedDate()), cellFont));

			// Actions cell
			PdfPCell actionsCell = new PdfPCell();
			actionsCell.addElement(new Phrase("Edit", cellFont));
			actionsCell.addElement(new Phrase("Delete", cellFont));
			actionsCell.setPadding(5);
			table.addCell(actionsCell);
		}

		document.add(table);
		document.close();

		return baos.toByteArray();
	}

	private PdfPCell createCell(String content, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(content, font));
		cell.setPadding(5);
		return cell;
	}
}
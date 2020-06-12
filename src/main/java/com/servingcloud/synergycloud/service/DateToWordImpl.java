package com.servingcloud.synergycloud.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfParagraphStyle;
import com.servingcloud.synergycloud.service.dto.InterFaceInfo;
import com.servingcloud.synergycloud.service.dto.RequestInfo;
import com.servingcloud.synergycloud.service.dto.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class DateToWordImpl {

    public void toWord(List<InterFaceInfo> list, String path) throws Exception {

        Document document = new Document(PageSize.A4);
        RtfWriter2.getInstance(document, new FileOutputStream(path));
        document.open();

        RtfParagraphStyle rtfGsBt1 = RtfParagraphStyle.STYLE_HEADING_1;
        rtfGsBt1.setAlignment(Element.ALIGN_CENTER);
        rtfGsBt1.setStyle(Font.BOLD);
        rtfGsBt1.setSize(12);

        Color chade = new Color(176, 196, 222);

        for (InterFaceInfo data : list) {

            //增加tag，并且设置字体，追加到文档结构图
            Paragraph paragraph1 = new Paragraph(data.getTag());
            paragraph1.setFont(rtfGsBt1);


            //new一个五列的table
            Table table = new Table(5);

            table.setBorderWidth(1);
            // table.setBorderColor(Color.BLACK);
            table.setPadding(0);
            table.setSpacing(0);

            //新增一个单元格
            Cell cell = new Cell(data.getTag());
            cell.setBackgroundColor(chade);
            //横跨5个单元格，即占满整行
            cell.setColspan(5);
            table.addCell(cell);

            //新增一个单元格，一行一列
            cell = new Cell("接口描述");
            table.addCell(cell);

            //新增一个单元格，一行四列，占满本行的横向剩余空间
            cell = new Cell(data.getDescription());
            cell.setColspan(4);
            table.addCell(cell);

            cell = new Cell("URL");
            table.addCell(cell);
            cell = new Cell(data.getUrl());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("请求方式");
            table.addCell(cell);
            cell = new Cell(data.getRequestType());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("请求类型");
            table.addCell(cell);
            cell = new Cell(data.getRequestForm());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("返回类型");
            table.addCell(cell);
            cell = new Cell(data.getResponseForm());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("参数名");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("数据类型");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("参数类型");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("是否必填");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("说明");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            for (RequestInfo req : data.getRequestList()) {
                cell = new Cell(req.getName());
                table.addCell(cell);
                cell = new Cell(req.getType());
                table.addCell(cell);
                cell = new Cell(req.getParamType());
                table.addCell(cell);
                cell = new Cell(req.getRequire());
                table.addCell(cell);
                cell = new Cell(req.getRemark());
                table.addCell(cell);
            }

            cell = new Cell("返回值字段");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("数据类型");
            cell.setBackgroundColor(chade);
            cell.setColspan(1);
            table.addCell(cell);

            cell = new Cell("说明");
            cell.setBackgroundColor(chade);
            cell.setColspan(3);
            table.addCell(cell);

            for (ResponseInfo res : data.getResponseList()) {

                cell = new Cell(res.getName());
                table.addCell(cell);

                cell = new Cell(res.getDescription());
                cell.setColspan(1);
                table.addCell(cell);

                cell = new Cell(res.getRemark());
                cell.setColspan(3);
                table.addCell(cell);
            }
            cell = new Cell("示例");
            cell.setBackgroundColor(chade);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell("请求参数");
            cell.setBackgroundColor(chade);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell(data.getRequestParam());
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell("返回值");
            cell.setBackgroundColor(chade);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell(data.getResponseParam());
            cell.setColspan(5);
            table.addCell(cell);

            paragraph1.add(table);
            document.add(paragraph1);
        }

        document.close();
    }

    public void toWord(List<InterFaceInfo> list, OutputStream outputStream) throws Exception {

        Document document = new Document(PageSize.A4);
        RtfWriter2.getInstance(document, outputStream);
        document.open();

        RtfParagraphStyle rtfGsBt1 = RtfParagraphStyle.STYLE_HEADING_1;
        rtfGsBt1.setAlignment(Element.ALIGN_CENTER);
        rtfGsBt1.setStyle(Font.BOLD);
        rtfGsBt1.setSize(12);

        Color chade = new Color(176, 196, 222);

        for (InterFaceInfo data : list) {

            //增加tag，并且设置字体，追加到文档结构图
            Paragraph paragraph1 = new Paragraph(data.getTag());
            paragraph1.setFont(rtfGsBt1);


            //new一个五列的table
            Table table = new Table(5);

            table.setBorderWidth(1);
            // table.setBorderColor(Color.BLACK);
            table.setPadding(0);
            table.setSpacing(0);

            //新增一个单元格
            Cell cell = new Cell(data.getTag());
            cell.setBackgroundColor(chade);
            //横跨5个单元格，即占满整行
            cell.setColspan(5);
            table.addCell(cell);

            //新增一个单元格，一行一列
            cell = new Cell("接口描述");
            table.addCell(cell);

            //新增一个单元格，一行四列，占满本行的横向剩余空间
            cell = new Cell(data.getDescription());
            cell.setColspan(4);
            table.addCell(cell);

            cell = new Cell("URL");
            table.addCell(cell);
            cell = new Cell(data.getUrl());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("请求方式");
            table.addCell(cell);
            cell = new Cell(data.getRequestType());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("请求类型");
            table.addCell(cell);
            cell = new Cell(data.getRequestForm());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("返回类型");
            table.addCell(cell);
            cell = new Cell(data.getResponseForm());
            cell.setColspan(4);
            table.addCell(cell);


            cell = new Cell("参数名");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("数据类型");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("参数类型");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("是否必填");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("说明");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            for (RequestInfo req : data.getRequestList()) {
                cell = new Cell(req.getName());
                table.addCell(cell);
                cell = new Cell(req.getType());
                table.addCell(cell);
                cell = new Cell(req.getParamType());
                table.addCell(cell);
                cell = new Cell(req.getRequire());
                table.addCell(cell);
                cell = new Cell(req.getRemark());
                table.addCell(cell);
            }

            cell = new Cell("返回值字段");
            cell.setBackgroundColor(chade);
            table.addCell(cell);

            cell = new Cell("数据类型");
            cell.setBackgroundColor(chade);
            cell.setColspan(1);
            table.addCell(cell);

            cell = new Cell("说明");
            cell.setBackgroundColor(chade);
            cell.setColspan(3);
            table.addCell(cell);

            for (ResponseInfo res : data.getResponseList()) {

                cell = new Cell(res.getName());
                table.addCell(cell);

                cell = new Cell(res.getDescription());
                cell.setColspan(1);
                table.addCell(cell);

                cell = new Cell(res.getRemark());
                cell.setColspan(3);
                table.addCell(cell);
            }
            cell = new Cell("示例");
            cell.setBackgroundColor(chade);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell("请求参数");
            cell.setBackgroundColor(chade);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell(data.getRequestParam());
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell("返回值");
            cell.setBackgroundColor(chade);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new Cell(data.getResponseParam());
            cell.setColspan(5);
            table.addCell(cell);

            paragraph1.add(table);
            document.add(paragraph1);
        }

        document.close();
    }
}

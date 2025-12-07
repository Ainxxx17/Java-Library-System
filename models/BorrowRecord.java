package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowRecord {

    private String memberId, memberName, bookId, bookTitle;
    private LocalDate borrowDate, dueDate, returnDate; // can be null
    private String status; // "Borrowed" or "Returned"
    private String remarks; // ex. "Late: 20 pesos" or "-"

    // formatter for reading/writing dates to/from the text file
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    // CONSTRUCTOR
    public BorrowRecord(String memberId, String memberName, String bookId, String bookTitle,
                        LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate,
                        String status, String remarks) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.remarks = remarks;
    }

    // GETTERS 
    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    // SETTERS
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // METHODS
    public String toString() {
        // handle null return date for file writing
        String returnDateStr = (returnDate == null) ? "-" : returnDate.format(DATE_FORMATTER);
        
        // Format: M1001,Ian Cabalquinto,B002,Dune,2025-11-14,2025-11-21,-,Borrowed,-
        return String.join(",",
                memberId,
                memberName,
                bookId,
                bookTitle,
                borrowDate.format(DATE_FORMATTER),
                dueDate.format(DATE_FORMATTER),
                returnDateStr,
                status,
                remarks
        );
    }
}
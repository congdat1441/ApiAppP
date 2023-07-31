package com.spring.carebookie.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Modifying
    @Query("update BookEntity b set b.status = 'CANCEL', b.message = :message, b.operationId = :operatorId where b.id = :id")
    void cancelBook(Long id, String message, String operatorId);

    @Modifying
    @Query(" update BookEntity b set b.status = 'ACCEPT', b.doctorId = :doctorId, " +
            " b.date = :date, b.dateExamination = :dateExamination, b.session = :session, b.operationId = :operatorId," +
            " b.message = :message where b.id = :id")
    void acceptBook(Long id, String doctorId, String date, LocalDate dateExamination, String session, String operatorId, String message);

    @Modifying
    @Query("update BookEntity b set b.status = 'CONFIRM', b.operationId = :operatorId where b.id = :id")
    void confirmBook(Long id, String operatorId);

    @Query("select sb.serviceId from BookEntity b \n" +
            "join ServiceBookEntity sb on b.id = sb.bookId \n" +
            "where b.id = :bookId")
    List<Long> getServiceIdsByBookId(Long bookId);

    @Query("select ish.invoiceId from BookEntity b \n" +
            "join InvoiceShareEntity ish on b.id = ish.bookId \n" +
            "where b.id = :bookId")
    List<Long> getInvoiceShareIdsByBookId(Long bookId);

    @Query("select b from BookEntity b where b.doctorId = ?1 and b.status ='ACCEPT' ")
    List<BookEntity> getBookAcceptByDoctorId(String doctorId);

    @Query("select b from BookEntity b where b.doctorId = ?1 and b.status ='CONFIRM' ")
    List<BookEntity> getBookConfirmByDoctorId(String doctorId);

    @Query("select b from BookEntity b where b.hospitalId = ?1 and b.status = 'PENDING' ")
    List<BookEntity> getAllBookPendingByHospitalId(String hospitalId);

    @Query("select b from BookEntity b where b.hospitalId = ?1 and b.status = 'ACCEPT' ")
    List<BookEntity> getAllBookAcceptByHospitalId(String hospitalId);

    @Query("select b from BookEntity b where b.hospitalId = ?1 and b.status = 'CANCEL' ")
    List<BookEntity> getAllBookCancelByHospitalId(String hospitalId);

    @Query("select b from BookEntity b where b.hospitalId = ?1 and b.status = 'CONFIRM' ")
    List<BookEntity> getAllBookConfirmByHospitalId(String hospitalId);

    @Query("select b from BookEntity b where b.userId = ?1 and b.status in ('CANCEL','PENDING','ACCEPT')")
    List<BookEntity> getAllBookByUserId(String userId);

    @Query("select b from BookEntity b where b.status in ('CANCEL','CONFIRM') and b.hospitalId = :hospitalId and year(b.dateTimeBook) = :year")
    List<BookEntity> getAllBookByHpAndYear(String hospitalId, Integer year);

}

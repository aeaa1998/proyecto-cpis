###TEMPLATE###

String_concat_ar_create:
	#Adding space for the parameters + body
	subi $sp, $sp, 16
	jr $ra
String_concat_ar_remove:
	#removing space for the parameters + body
	subi $sp, $sp, -16
	jr $ra

String_concat_state_save:
    #Saving
	subi $sp, $sp, 24
    sw $t1, 0($sp)
	sw $t2, 4($sp)
	sw $t3, 8($sp),
	sw $t4, 12($sp)
	sw $t0, 16($sp)
	sw $t9, 20($sp)
	jr $ra
String_concat_state_restore:
	#rRestoring
    lw $t1, 0($sp)
	lw $t2, 4($sp)
	lw $t3, 8($sp)
	lw $t4, 12($sp)
	lw $t0, 16($sp)
	lw $t9, 20($sp)
    addi $sp, $sp, 24
	jr $ra

#param string 1
#param string 2
String_concat:
    sw $ra, -28($sp)
    jal String_concat_state_save
    jal String_concat_ar_create
	# load strings
 	lw $t0, 0($sp)
	lw $t1, 4($sp)
	
	# load strings addresses and set the lengths
    sw $t0, -24($sp)
    jal String_length_state_save
    jal String_length_ar_create
	jal String_length
    jal String_length_ar_remove
    jal String_length_state_restore
	move $t2, $v0
	
	sw $t1, -24($sp)
    jal String_length_state_save
    jal String_length_ar_create
	jal String_length
    jal String_length_ar_remove
    jal String_length_state_restore
	#total size
	add $t2, $v0, $t2
	
	
	# alloc in heap
	li $v0, 9
	move $a0, $t2
	syscall
	
	# load heap address
	la $t3, ($v0)
	
    concat_loop_first_text:
        lb $t4, 0($t0)
        sb $t4, 0($t3)
        
        # is end of string
        beq $t4, $zero, concat_loop_second_text
        addi $t0, $t0, 1
        addi $t3, $t3, 1
        
        j concat_loop_first_text
    concat_loop_second_text:
        lb $t4, 0($t1)
        sb $t4, 0($t3)
        
        # is end of string
        beq $t4, $zero, end_concat_string
        addi $t1, $t1, 1
        addi $t3, $t3, 1
        j concat_loop_second_text
    end_concat_string:
        sub $t3, $t3, $t2
        la $v0, ($t3)
        jal String_concat_ar_remove
        jal String_concat_state_restore
        lw $ra, -28($sp)
        jr $ra

String_substr_ar_create:
	#Adding space for the parameters + body
	subi $sp, $sp, 16
	jr $ra

String_substr_ar_remove:
	#Adding space for the parameters + body
	addi $sp, $sp, 16
	jr $ra

String_substr_state_save:
	sub $sp, $sp, 24
	# Save the state
	sw $t1, ($sp)
	sw $t2, 4($sp)
	sw $t3, 8($sp),
	sw $t4, 12($sp)
	sw $t0, 16($sp)
	sw $t9, 20($sp)
	jr $ra

String_substr_state_restore:
	# Restore the state
	sw $t1, ($sp)
	sw $t2, 4($sp)
	sw $t3, 8($sp),
	sw $t4, 12($sp)
	sw $t0, 16($sp)
	sw $t9, 20($sp)
	addi $sp, $sp, 24
	jr $ra

# text address
# start 
# end 
String_substr:
	sw $ra, -28($sp)
    jal String_substr_state_save
    jal String_substr_ar_create
	# text address load
	lw $t0, 0($sp)
	lw $a1, 4($sp)
	lw $a2, 8($sp)

    #It is invalid so we send a error
    blt $a2, $a1, Substr_substr_invalid

	#Size of string
	sub $t3, $a2, $a1
	li $v0, 9 #call heap alloc instruction
	move $a0, $t3 #assign needed bytes on heap
	syscall
	#heap address load
	la $t1,($v0)
	
	#add initial offset
	add $t0, $t0, $a1
	
    String_loop_substr:
        lb $t2, 0($t0)
        sb $t2, 0($t1)
        #Advance in string
        addi $t1, $t1, 1
        addi $t0, $t0, 1
        addi $a1, $a1, 1
        
        beq $a1, $a2, end_subtring
        
        j String_loop_substr

        
    end_subtring:
        
        sub $t1, $t1, $t3
        la $v0, ($t1)

        jal String_substr_ar_remove
        jal String_substr_state_restore
        lw $ra, -28($sp)

        jr $ra
    Substr_substr_invalid:
        la $a0, invalid_string
        li $v0, 4
        syscall

        li $v0, 10
        syscall

String_length_ar_create:
	#Adding space for the parameters + body
	subi $sp, $sp, 8
	jr $ra
String_length_ar_remove:
	#removing space for the parameters + body
	subi $sp, $sp, -8
	jr $ra

String_length_state_save:
    #Saving
	subi $sp, $sp, 16
    sw $t0, 0($sp)
    sw $t1, 4($sp)
	sw $t2, 8($sp)
	sw $t9, 12($sp)
	jr $ra
String_length_state_restore:
	#rRestoring
	lw $t0, 0($sp)
    lw $t1, 4($sp)
	lw $t2, 8($sp)
	lw $t9, 12($sp)
    addi $sp, $sp, 16
	jr $ra

#string address
String_length:
	sw $ra, -20($sp)
    jal String_length_state_save
    jal String_length_ar_create
	
	la $t0, 0($sp)
	li $t1, 0
    str_len_loop:
        # load char
        lb $t2, 0($t0)
        beq $t2, $zero, end_str_len
        addi $t1, $t1, 1
        addi $t0, $t0, 1
	j str_len_loop
    end_str_len:
    move $v0, $t1
    
    jal String_length_ar_remove
    jal String_length_state_restore
    lw $ra, -20($sp)
    jr $ra
	
# paString size
# text address
copyString:

	sub $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $ra, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	

	la $t0, ($a1)
	
	li $v0, 9 #call heap alloc instruction
	syscall
	
	la $t1, ($v0)
	
    loop:
        lb $t2, 0($t0)
        sb $t2, 0($t1)
        #Advance in string
        addi $t1, $t1, 1
        addi $t0, $t0, 1
        
        beq $t2, $zero, end_copy
        
        j loop
        
    end_copy:
        sub $t1, $t1, 1
        sub $t1, $t1, $a0
        la $v0, ($t1)
        # We are going to restore the current state
        lw $t9, 0($sp)
        lw $ra, 4($sp)
        lw $t0, 8($sp)
        lw $t1, 12($sp)
        lw $t2, 16($sp)
        add $sp, $sp, 20
        
        jr	$ra
	
out_string:
    sub $sp, $sp, 4
	sw $t9, 0($sp)
    sub $sp, $sp, 12
	sw $ra, 8($sp)
	lw $a0, 4($sp)
	li $v0, 4
	syscall 
    # Must return itself
    # lw $v0, 0($sp)
	lw $ra, 8($sp)
    sub $sp, $sp, -12
    lw $t9, 0($sp)
	sub $sp, $sp, -4
	jr $ra


out_int_state_save: 
	sub $sp, $sp, 8
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	jr $ra

out_int_state_restore:
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	sub $sp, $sp, -8
	jr $ra

out_int_ar_create:
	#Adding space for the parameters + body
	sub $sp, $sp, 12
	jr $ra

out_int_ar_remove:
	#REmovin space for the parameters + body
	sub $sp, $sp, -12
	jr $ra

out_int:
	sw $ra, -12($sp)
    jal out_int_state_save
    jal out_int_ar_create
	#We load the int
	lw $a0, 4($sp)
	li $v0, 1
	syscall 
    # Must load itself
    # lw $v0, 0($sp)
    jal out_int_ar_remove
    jal out_int_state_restore
	lw $ra, -12($sp)
	jr $ra

in_string:
    jal in_string_state_save
    jal in_string_ar_create
	sw $ra, 8($sp)
	
	#Call space
	li $v0, 9
	li $a0, 255
	syscall
	
	#Put the heap value
	move $a0, $v0
	li $a1, 255
	li $v0, 8
	syscall 
	
	move $v0, $a0
	
	lw $ra, 8($sp)
    jal in_string_ar_remove
    jal in_string_state_restore
	jr $ra

in_string_state_save: 
	sub $sp, $sp, 12
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	jr $ra

in_string_state_restore:
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	sub $sp, $sp, -12
	jr $ra

in_string_ar_create:
	#Adding space for the parameters + body
	sub $sp, $sp, 12
	jr $ra

in_string_ar_remove:
	#REmovin space for the parameters + body
	sub $sp, $sp, -12
	jr $ra
	
in_int:
    jal in_int_state_save
    jal in_int_ar_create
	sw $ra, 8($sp)
	
	li $v0, 5
	syscall 
	
	lw $ra, 8($sp)
    jal in_int_ar_remove
    jal in_int_state_restore
	jr $ra

in_int_state_save: 
	subi $sp, $sp, 4
	sw $t9, 0($sp)
	jr $ra

in_int_state_restore:
	lw $t9, 0($sp)
	subi $sp, $sp, -4
	jr $ra

in_int_ar_create:
	#Adding space for the parameters + body
	subi $sp, $sp, 12
	jr $ra

in_int_ar_remove:
	#REmovin space for the parameters + body
	subi $sp, $sp, -12
	jr $ra



    


Object_type_name:
    subi $sp, $sp, 4
    sw $t9, 0($sp)

    subi $sp, $sp, 8
    sw $ra, 4($sp)
	
    lw $v0, 0($sp)
    # We load again but this time the string
    lw $v0, 0($v0)
	
	lw $ra, 4($sp)
    addi $sp, $sp, 8

    lw $t9, 0($sp)
    addi $sp, $sp, 4

	jr $ra

instantiate_new_Object:
	subi $sp, $sp, 12
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	li $v0, 9
	li $a0, 8
	syscall
	move $t9, $v0
	#Declaring name value
	li $a0, 6
	la $a1, objectName
	jal copyString
	sw $v0,  0($t9)
    la $v0, Object_vtable
    sw $v0, 4($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	addi $sp, $sp, 12
	jr $ra


Bool_type_name:
    subi $sp, $sp, 4
    sw $t9, 0($sp)
    subi $sp, $sp, 8

    sw $ra, 4($sp)
	
    li $a0, 4
	la $a1, boolName
	jal copyString
	
	lw $ra, 4($sp)
    addi $sp, $sp, 8
    lw $t9, 0($sp)
    addi $sp, $sp, 4
	jr $ra


Int_type_name:
    subi $sp, $sp, 4
    sw $t9, 0($sp)
    subi $sp, $sp, 8

    sw $ra, 4($sp)
	
    li $a0, 3
	la $a1, intName
	jal copyString
	
	lw $ra, 4($sp)

    addi $sp, $sp, 8

    lw $t9, 0($sp)
    addi $sp, $sp, 4
	jr $ra


String_type_name:
    subi $sp, $sp, 4
    sw $t9, 0($sp)

    subi $sp, $sp, 8

    sw $ra, 4($sp)
	
    li $a0, 6
	la $a1, stringName
	jal copyString
	
	lw $ra, 4($sp)

    addi $sp, $sp, 8

    lw $t9, 0($sp)
    addi $sp, $sp, 4

	jr $ra



Object_abort:
    subi $sp, $sp, 4
    sw $t9, 0($sp)

    subi $sp, $sp, 8

    sw $ra, 4($sp)
	
    la $a0, abortMessage
	li $v0, 4
    syscall

    li $v0, 10
    syscall
	
	lw $ra, 4($sp)

    lw $t9, 0($sp)
    addi $sp, $sp, 4

    addi $sp, $sp, 8

	jr $ra


String_abort:
    subi $sp, $sp, 4
    sw $t9, 0($sp)

    subi $sp, $sp, 8

    sw $ra, 4($sp)
    # We send the string
	lw $a0, 0($sp)
	li $v0, 4
    syscall

    li $v0, 10
    syscall
	
	lw $ra, 4($sp)

    addi $sp, $sp, 8

    lw $t9, 0($sp)
    addi $sp, $sp, 4

	jr $ra

.data
	invalid_string: .asciiz "Los indices pasados son inválidos"
	objectName: .asciiz "Object"
	stringName: .asciiz "String"
	intName: .asciiz "Int"
	boolName: .asciiz "Bool"
	abortMessage: .asciiz "Interrupción por algún error"
	Object_vtable:
		.word Object_abort
		.word Object_type_name
	string0: .asciiz "espar!\n"
	string1: .asciiz "esimpar!\n"
	string2: .asciiz "\n"
	string3: .asciiz "\n"
	string4: .asciiz "\n"
	string5: .asciiz "\n"
	string6: .asciiz "\n"
	string7: .asciiz "Main"
	string8: .asciiz "A"
	string9: .asciiz "B"
	string10: .asciiz "C"
	string11: .asciiz "D"
	string12: .asciiz "E"
	Main_vtable:
		.word Object_abort
		.word Object_type_name
		.word out_string
		.word out_int
		.word in_string
		.word in_int
		.word Main_main
		.word Main_is_even
	A_vtable:
		.word Object_abort
		.word Object_type_name
		.word A_value
		.word A_set_var
		.word A_method1
		.word A_method2
		.word A_method3
		.word A_method4
		.word A_method5
	B_vtable:
		.word Object_abort
		.word Object_type_name
		.word A_value
		.word A_set_var
		.word A_method1
		.word A_method2
		.word A_method3
		.word A_method4
		.word B_method5
	C_vtable:
		.word Object_abort
		.word Object_type_name
		.word A_value
		.word A_set_var
		.word A_method1
		.word A_method2
		.word A_method3
		.word A_method4
		.word C_method5
		.word C_method6
	D_vtable:
		.word Object_abort
		.word Object_type_name
		.word A_value
		.word A_set_var
		.word A_method1
		.word A_method2
		.word A_method3
		.word A_method4
		.word B_method5
		.word D_method7
	E_vtable:
		.word Object_abort
		.word Object_type_name
		.word A_value
		.word A_set_var
		.word A_method1
		.word A_method2
		.word A_method3
		.word A_method4
		.word B_method5
		.word D_method7
		.word E_method6
.text
main:
jal instantiate_new_Main
move $t9, $v0
sw $t9, -320($sp)
jal Main_main
li $v0, 10
syscall
#Declaring new function main
Main_main:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	li $t0, 1
	sw $t0, 4($sp)
	lw $t1, 8($sp)
	nor $t1, $t0, $t0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t1, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	lw $t2, 12($sp)
	#Setting value for out_int
	move $t2, $v0
	#Creating a new instance of the class A
	jal instantiate_new_A
	sw $t0, 4($sp)
	lw $t0, 12($t9)
	#Setting value for instantiate_new_A
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	sw $t0, 12($t9)
	lw $t0, 16($sp)
	li $t0, 2
	sw $t0, 16($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling avar.set_var(2)
	sw $t0, 16($sp)
	lw $t0, 12($t9)
	lw $s0, 4($t0)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 12($t9)
	lw $t0, 20($sp)
	#Setting value for A_set_var
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 20($sp)
	lw $t0, 12($t9)
	sw $t0, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t0)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 12($t9)
	lw $t0, 24($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t0, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 24($sp)
	lw $t0, 28($sp)
	#Setting value for out_int
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 28($sp)
	lw $t0, 12($t9)
	sw $t0, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t0)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 12($t9)
	lw $t0, 32($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -320($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	lw $s0, 4($t9)
	lw $s0, 28($s0)
	jalr $s0
	sw $t0, 32($sp)
	lw $t0, 36($sp)
	#Setting value for Main_is_even
	move $t0, $v0
	bne $t0, 0, TRUE_0
	j FALSE_0
	TRUE_0:
sw $t0, 36($sp)
sw $t1, 8($sp)
sw $t2, 12($sp)
		sw $t0, 36($sp)
		lw $t0, 40($sp)
		li $a0, 7
		la $a1, string0
		jal copyString
		move $t0, $v0
		#We are going to send a heap
		sw $t9, -16($sp)
		#We are going to send a param with offset defined
		sw $t0, -12($sp)
		lw $s0, 4($t9)
		lw $s0, 8($s0)
		jalr $s0
		sw $t0, 40($sp)
		lw $t0, 44($sp)
		#Setting value for out_string
		move $t0, $v0
		sw $t1, 8($sp)
		lw $t1, 56($sp)
		sw $t0, 56($sp)
		move $t1, $t0
lw $t0, 36($sp)
lw $t1, 8($sp)
lw $t2, 12($sp)
		j ENDIF_0
	FALSE_0:
sw $t0, 36($sp)
sw $t1, 8($sp)
sw $t2, 12($sp)
		sw $t0, 36($sp)
		lw $t0, 48($sp)
		li $a0, 9
		la $a1, string1
		jal copyString
		move $t0, $v0
		#We are going to send a heap
		sw $t9, -16($sp)
		#We are going to send a param with offset defined
		sw $t0, -12($sp)
		lw $s0, 4($t9)
		lw $s0, 8($s0)
		jalr $s0
		sw $t0, 48($sp)
		lw $t0, 52($sp)
		#Setting value for out_string
		move $t0, $v0
		sw $t1, 8($sp)
		lw $t1, 56($sp)
		sw $t0, 56($sp)
		move $t1, $t0
	ENDIF_0:
	lw $t0, 36($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	#Creating a new instance of the class A
	jal instantiate_new_A
	sw $t0, 36($sp)
	lw $t0, 60($sp)
	#Setting value for instantiate_new_A
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	sw $t0, 60($sp)
	lw $t0, 64($sp)
	li $t0, 3
	sw $t0, 64($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling (newA).set_var(3)
	sw $t0, 64($sp)
	lw $t0, 60($sp)
	lw $s0, 4($t0)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 60($sp)
	lw $t0, 68($sp)
	#Setting value for A_set_var
	move $t0, $v0
	sw $t1, 8($sp)
	lw $t1, 16($t9)
	sw $t0, 16($t9)
	move $t1, $t0
	#Creating a new instance of the class B
	jal instantiate_new_B
	sw $t0, 68($sp)
	sw $t0, 16($t9)
	lw $t0, 72($sp)
	#Setting value for instantiate_new_B
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 72($sp)
	lw $t0, 12($t9)
	sw $t0, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t0)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 12($t9)
	lw $t0, 76($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling a_var.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 76($sp)
	lw $t0, 80($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 80($sp)
	lw $t0, 72($sp)
	sw $t0, -320($sp)
	sw $t0, 72($sp)
	lw $t0, 76($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	sw $t0, 76($sp)
	lw $t0, 80($sp)
	#We are going to send a param with offset defined
	sw $t0, -312($sp)
	##Calling (newB).method2(avar.value(),a_var.value())
	jal A_method2
	sw $t0, 80($sp)
	lw $t0, 84($sp)
	#Setting value for A_method2
	move $t0, $v0
	sw $t1, 16($t9)
	lw $t1, 12($t9)
	sw $t0, 12($t9)
	move $t1, $t0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 84($sp)
	sw $t0, 12($t9)
	lw $t0, 88($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t0, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 88($sp)
	lw $t0, 92($sp)
	#Setting value for out_int
	move $t0, $v0
	sw $t0, 92($sp)
	lw $t0, 96($sp)
	li $a0, 1
	la $a1, string2
	jal copyString
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -16($sp)
	#We are going to send a param with offset defined
	sw $t0, -12($sp)
	lw $s0, 4($t9)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 96($sp)
	lw $t0, 100($sp)
	#Setting value for out_string
	move $t0, $v0
	#Creating a new instance of the class C
	jal instantiate_new_C
	sw $t0, 100($sp)
	lw $t0, 104($sp)
	#Setting value for instantiate_new_C
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 104($sp)
	lw $t0, 108($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 108($sp)
	lw $t0, 104($sp)
	sw $t0, -320($sp)
	sw $t0, 104($sp)
	lw $t0, 108($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling (newC).method6(avar.value())
	sw $t0, 108($sp)
	lw $t0, 104($sp)
	lw $s0, 4($t0)
	lw $s0, 36($s0)
	jalr $s0
	sw $t0, 104($sp)
	lw $t0, 112($sp)
	#Setting value for C_method6
	move $t0, $v0
	sw $t0, 12($t9)
	move $t1, $t0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 112($sp)
	sw $t0, 12($t9)
	lw $t0, 116($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t0, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 116($sp)
	lw $t0, 120($sp)
	#Setting value for out_int
	move $t0, $v0
	sw $t0, 120($sp)
	lw $t0, 124($sp)
	li $a0, 1
	la $a1, string3
	jal copyString
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -16($sp)
	#We are going to send a param with offset defined
	sw $t0, -12($sp)
	lw $s0, 4($t9)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 124($sp)
	lw $t0, 128($sp)
	#Setting value for out_string
	move $t0, $v0
	#Creating a new instance of the class A
	jal instantiate_new_A
	sw $t0, 128($sp)
	lw $t0, 132($sp)
	#Setting value for instantiate_new_A
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	sw $t0, 132($sp)
	lw $t0, 136($sp)
	li $t0, 5
	sw $t0, 136($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling (newA).set_var(5)
	sw $t0, 136($sp)
	lw $t0, 132($sp)
	lw $s0, 4($t0)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 132($sp)
	lw $t0, 140($sp)
	#Setting value for A_set_var
	move $t0, $v0
	sw $t1, 12($t9)
	lw $t1, 16($t9)
	sw $t0, 16($t9)
	move $t1, $t0
	#Creating a new instance of the class D
	jal instantiate_new_D
	sw $t0, 140($sp)
	sw $t0, 16($t9)
	lw $t0, 144($sp)
	#Setting value for instantiate_new_D
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 144($sp)
	lw $t0, 12($t9)
	sw $t0, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t0)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 12($t9)
	lw $t0, 148($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling a_var.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 148($sp)
	lw $t0, 152($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 152($sp)
	lw $t0, 144($sp)
	sw $t0, -320($sp)
	sw $t0, 144($sp)
	lw $t0, 148($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	sw $t0, 148($sp)
	lw $t0, 152($sp)
	#We are going to send a param with offset defined
	sw $t0, -312($sp)
	##Calling (newD).method4(avar.value(),a_var.value())
	jal A_method4
	sw $t0, 152($sp)
	lw $t0, 156($sp)
	#Setting value for A_method4
	move $t0, $v0
	sw $t1, 16($t9)
	lw $t1, 12($t9)
	sw $t0, 12($t9)
	move $t1, $t0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 156($sp)
	sw $t0, 12($t9)
	lw $t0, 160($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t0, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 160($sp)
	lw $t0, 164($sp)
	#Setting value for out_int
	move $t0, $v0
	sw $t0, 164($sp)
	lw $t0, 168($sp)
	li $a0, 1
	la $a1, string4
	jal copyString
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -16($sp)
	#We are going to send a param with offset defined
	sw $t0, -12($sp)
	lw $s0, 4($t9)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 168($sp)
	lw $t0, 172($sp)
	#Setting value for out_string
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	sw $t0, 172($sp)
	lw $t0, 176($sp)
	li $t0, 5
	sw $t0, 176($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling avar.set_var(5)
	lw $s0, 4($t1)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 176($sp)
	lw $t0, 180($sp)
	#Setting value for A_set_var
	move $t0, $v0
	#Creating a new instance of the class C
	jal instantiate_new_C
	sw $t0, 180($sp)
	lw $t0, 184($sp)
	#Setting value for instantiate_new_C
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 184($sp)
	lw $t0, 188($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 188($sp)
	lw $t0, 184($sp)
	sw $t0, -320($sp)
	sw $t0, 184($sp)
	lw $t0, 188($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling (newC)@A.method5(avar.value())
	jal A_method5
	sw $t0, 188($sp)
	lw $t0, 192($sp)
	#Setting value for A_method5
	move $t0, $v0
	sw $t0, 12($t9)
	move $t1, $t0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 192($sp)
	sw $t0, 12($t9)
	lw $t0, 196($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t0, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 196($sp)
	lw $t0, 200($sp)
	#Setting value for out_int
	move $t0, $v0
	sw $t0, 200($sp)
	lw $t0, 204($sp)
	li $a0, 1
	la $a1, string5
	jal copyString
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -16($sp)
	#We are going to send a param with offset defined
	sw $t0, -12($sp)
	lw $s0, 4($t9)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 204($sp)
	lw $t0, 208($sp)
	#Setting value for out_string
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	sw $t0, 208($sp)
	lw $t0, 212($sp)
	li $t0, 6
	sw $t0, 212($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling avar.set_var(6)
	lw $s0, 4($t1)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 212($sp)
	lw $t0, 216($sp)
	#Setting value for A_set_var
	move $t0, $v0
	#Creating a new instance of the class C
	jal instantiate_new_C
	sw $t0, 216($sp)
	lw $t0, 220($sp)
	#Setting value for instantiate_new_C
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 220($sp)
	lw $t0, 224($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, 224($sp)
	lw $t0, 220($sp)
	sw $t0, -320($sp)
	sw $t0, 220($sp)
	lw $t0, 224($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling (newC)@B.method5(avar.value())
	jal B_method5
	sw $t0, 224($sp)
	lw $t0, 228($sp)
	#Setting value for B_method5
	move $t0, $v0
	sw $t0, 12($t9)
	move $t1, $t0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t1, -320($sp)
	##Calling avar.value()
	lw $s0, 4($t1)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 228($sp)
	sw $t0, 12($t9)
	lw $t0, 232($sp)
	#Setting value for A_value
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -20($sp)
	#We are going to send a param with offset defined
	sw $t0, -16($sp)
	lw $s0, 4($t9)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 232($sp)
	lw $t0, 236($sp)
	#Setting value for out_int
	move $t0, $v0
	sw $t0, 236($sp)
	lw $t0, 240($sp)
	li $a0, 1
	la $a1, string6
	jal copyString
	move $t0, $v0
	#We are going to send a heap
	sw $t9, -16($sp)
	#We are going to send a param with offset defined
	sw $t0, -12($sp)
	lw $s0, 4($t9)
	lw $s0, 8($s0)
	jalr $s0
	sw $t0, 240($sp)
	lw $t0, 244($sp)
	#Setting value for out_string
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función main
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function is_even
Main_is_even:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	sw $t0, 8($sp)
	move $t1, $t0
	lw $t2, 12($sp)
	slti $t2, $t1, 0
	bne $t2, 0, TRUE_1
	j FALSE_1
	TRUE_1:
sw $t0, 4($sp)
sw $t0, 8($sp)
sw $t1, 8($sp)
sw $t2, 12($sp)
		sw $t0, 4($sp)
		sw $t0, 8($sp)
		lw $t0, 16($sp)
		nor $t0, $t1, $t1
		#We are going to send a heap
		sw $t9, -320($sp)
		#We are going to send a param with offset defined
		sw $t0, -316($sp)
		lw $s0, 4($t9)
		lw $s0, 28($s0)
		jalr $s0
		sw $t0, 16($sp)
		lw $t0, 20($sp)
		#Setting value for Main_is_even
		move $t0, $v0
		sw $t1, 8($sp)
		lw $t1, 56($sp)
		sw $t0, 56($sp)
		move $t1, $t0
lw $t0, 4($sp)
lw $t1, 8($sp)
lw $t2, 12($sp)
		j ENDIF_1
	FALSE_1:
sw $t0, 4($sp)
sw $t0, 8($sp)
sw $t1, 8($sp)
sw $t2, 12($sp)
		sw $t0, 4($sp)
		sw $t0, 8($sp)
		lw $t0, 28($sp)
		li $t0, 0
		sw $t0, 28($sp)
		sw $t1, 8($sp)
		lw $t1, 8($sp)
		sw $t2, 12($sp)
		lw $t2, 24($sp)
		xor $t2, $t0, $t1
		slti $t2, $t2, 1
		bne $t2, 0, TRUE_2
		j FALSE_2
		TRUE_2:
sw $t0, 28($sp)
sw $t1, 8($sp)
sw $t2, 24($sp)
			sw $t0, 28($sp)
			lw $t0, 52($sp)
			li $t0, 1
			sw $t0, 52($sp)
lw $t0, 28($sp)
lw $t1, 8($sp)
lw $t2, 24($sp)
			j ENDIF_2
		FALSE_2:
sw $t0, 28($sp)
sw $t1, 8($sp)
sw $t2, 24($sp)
			sw $t0, 28($sp)
			lw $t0, 36($sp)
			li $t0, 1
			sw $t0, 36($sp)
			sw $t2, 24($sp)
			lw $t2, 32($sp)
			xor $t2, $t0, $t1
			slti $t2, $t2, 1
			bne $t2, 0, TRUE_3
			j FALSE_3
			TRUE_3:
sw $t0, 36($sp)
sw $t1, 8($sp)
sw $t2, 32($sp)
				sw $t0, 36($sp)
				lw $t0, 48($sp)
				li $t0, 0
				sw $t0, 48($sp)
lw $t0, 36($sp)
lw $t1, 8($sp)
lw $t2, 32($sp)
				j ENDIF_3
			FALSE_3:
sw $t0, 36($sp)
sw $t1, 8($sp)
sw $t2, 32($sp)
				sw $t0, 36($sp)
				lw $t0, 40($sp)
				subi $t0, $t1, 2
				#We are going to send a heap
				sw $t9, -320($sp)
				#We are going to send a param with offset defined
				sw $t0, -316($sp)
				lw $s0, 4($t9)
				lw $s0, 28($s0)
				jalr $s0
				sw $t0, 40($sp)
				lw $t0, 44($sp)
				#Setting value for Main_is_even
				move $t0, $v0
				sw $t1, 8($sp)
				lw $t1, 48($sp)
				sw $t0, 48($sp)
				move $t1, $t0
			ENDIF_3:
			lw $t0, 36($sp)
			lw $t1, 8($sp)
			lw $t2, 32($sp)
			sw $t0, 36($sp)
			lw $t0, 48($sp)
			sw $t1, 8($sp)
			lw $t1, 52($sp)
			sw $t0, 52($sp)
			move $t1, $t0
		ENDIF_2:
		lw $t0, 28($sp)
		lw $t1, 8($sp)
		lw $t2, 24($sp)
		sw $t0, 28($sp)
		lw $t0, 52($sp)
		sw $t1, 8($sp)
		lw $t1, 56($sp)
		sw $t0, 56($sp)
		move $t1, $t0
	ENDIF_1:
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función is_even
	sw $t0, 4($sp)
	sw $t0, 8($sp)
	lw $t0, 56($sp)
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Defining new class Main
instantiate_new_Main:
	subi $sp, $sp, 20
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	li $v0, 9
	li $a0, 28
	syscall
	move $t9, $v0
	la $v0, Main_vtable
	sw $v0, 4($t9)
	#Declaring property  flag of class Bool
	lw $t0, 20($t9)
	li $t0, 1
	sw $t0, 20($t9)
	#Declaring name value
	li $a0, 4
	la $a1, string7
	jal copyString
	sw $v0,  0($t9)
	sw $t0, 20($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	lw $t1, 12($sp)
	lw $t2, 16($sp)
	addi $sp, $sp, 20
	jr $ra
#Declaring new function value
A_value:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función value
	lw $t0, 8($t9)
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function set_var
A_set_var:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($t9)
	sw $t0, 8($t9)
	move $t1, $t0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función set_var
	move $v0, $t9
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function method1
A_method1:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method1
	move $v0, $t9
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function method2
A_method2:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 16($sp)
	add $t2, $t0, $t1
	sw $t0, 4($sp)
	lw $t0, 12($sp)
	sw $t2, 12($sp)
	move $t0, $t2
	#Creating a new instance of the class B
	jal instantiate_new_B
	sw $t0, 12($sp)
	lw $t0, 20($sp)
	#Setting value for instantiate_new_B
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	#We are going to send a param with offset defined
	sw $t2, -316($sp)
	##Calling (newB).set_var(x)
	jal A_set_var
	sw $t0, 20($sp)
	lw $t0, 24($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method2
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function method3
A_method3:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 12($sp)
	nor $t1, $t0, $t0
	lw $t2, 8($sp)
	sw $t1, 8($sp)
	move $t2, $t1
	#Creating a new instance of the class C
	jal instantiate_new_C
	sw $t0, 4($sp)
	lw $t0, 16($sp)
	#Setting value for instantiate_new_C
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	#We are going to send a param with offset defined
	sw $t2, -316($sp)
	##Calling (newC).set_var(x)
	jal A_set_var
	sw $t0, 16($sp)
	lw $t0, 20($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method3
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function method4
A_method4:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	lw $t0, 8($sp)
	lw $t1, 4($sp)
	lw $t2, 12($sp)
	slt $t2, $t0, $t1
	bne $t2, 0, TRUE_4
	j FALSE_4
	TRUE_4:
sw $t0, 8($sp)
sw $t1, 4($sp)
sw $t2, 12($sp)
		#Entrando a scope de let
		sw $t2, 12($sp)
		lw $t2, 20($sp)
		sub $t2, $t1, $t0
		sw $t0, 8($sp)
		lw $t0, 16($sp)
		sw $t2, 16($sp)
		move $t0, $t2
		#Creating a new instance of the class D
		jal instantiate_new_D
		sw $t0, 16($sp)
		lw $t0, 24($sp)
		#Setting value for instantiate_new_D
		move $t0, $v0
		#We are going to pass an instance as a param
		#We are going to send a heap
		sw $t0, -320($sp)
		#We are going to send a param with offset defined
		sw $t2, -316($sp)
		##Calling (newD).set_var(x)
		jal A_set_var
		sw $t0, 24($sp)
		lw $t0, 28($sp)
		#Setting value for A_set_var
		move $t0, $v0
		sw $t1, 4($sp)
		lw $t1, 48($sp)
		sw $t0, 48($sp)
		move $t1, $t0
lw $t0, 8($sp)
lw $t1, 4($sp)
lw $t2, 12($sp)
		j ENDIF_4
	FALSE_4:
sw $t0, 8($sp)
sw $t1, 4($sp)
sw $t2, 12($sp)
		#Entrando a scope de let
		sw $t0, 8($sp)
		lw $t0, 8($sp)
		sw $t1, 4($sp)
		lw $t1, 4($sp)
		sw $t2, 12($sp)
		lw $t2, 36($sp)
		sub $t2, $t0, $t1
		sw $t0, 8($sp)
		lw $t0, 32($sp)
		sw $t2, 32($sp)
		move $t0, $t2
		#Creating a new instance of the class D
		jal instantiate_new_D
		sw $t0, 32($sp)
		lw $t0, 40($sp)
		#Setting value for instantiate_new_D
		move $t0, $v0
		#We are going to pass an instance as a param
		#We are going to send a heap
		sw $t0, -320($sp)
		#We are going to send a param with offset defined
		sw $t2, -316($sp)
		##Calling (newD).set_var(x)
		jal A_set_var
		sw $t0, 40($sp)
		lw $t0, 44($sp)
		#Setting value for A_set_var
		move $t0, $v0
		sw $t1, 4($sp)
		lw $t1, 48($sp)
		sw $t0, 48($sp)
		move $t1, $t0
	ENDIF_4:
	lw $t0, 8($sp)
	lw $t1, 4($sp)
	lw $t2, 12($sp)
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method4
	sw $t0, 8($sp)
	lw $t0, 48($sp)
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function method5
A_method5:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 8($sp)
	li $t0, 1
	sw $t0, 8($sp)
	#Entrando a scope de let
	lw $t1, 12($sp)
	li $t1, 1
	sw $t1, 12($sp)
	lw $t2, 4($sp)
	sw $t0, 8($sp)
	lw $t0, 16($sp)
	slt $t0, $t2, $t1
	addi $t0, $t0, 1
	div $t0, $t0, 2
	mfhi $t0
	bne $t0, 0, WHILE_0
	j END_WHILE_0
sw $t0, 16($sp)
sw $t1, 12($sp)
sw $t2, 4($sp)
	WHILE_0:
		sw $t0, 16($sp)
		lw $t0, 8($sp)
		sw $t2, 4($sp)
		lw $t2, 20($sp)
		mul $t2, $t0, $t1
		sw $t2, 8($sp)
		move $t0, $t2
		sw $t0, 8($sp)
		lw $t0, 24($sp)
		addi $t0, $t1, 1
		sw $t0, 12($sp)
		move $t1, $t0
		sw $t0, 24($sp)
		sw $t0, 12($sp)
		lw $t0, 4($sp)
		sw $t2, 20($sp)
		sw $t2, 8($sp)
		lw $t2, 28($sp)
		slt $t2, $t0, $t1
		addi $t2, $t2, 1
		div $t2, $t2, 2
		mfhi $t2
	bne $t2, 0, WHILE_0
lw $t0, 16($sp)
lw $t1, 12($sp)
lw $t2, 4($sp)
	END_WHILE_0:
	#Creating a new instance of the class E
	jal instantiate_new_E
	sw $t0, 16($sp)
	lw $t0, 36($sp)
	#Setting value for instantiate_new_E
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	sw $t0, 36($sp)
	lw $t0, 8($sp)
	#We are going to send a param with offset defined
	sw $t0, -316($sp)
	##Calling (newE).set_var(x)
	jal A_set_var
	sw $t0, 8($sp)
	lw $t0, 40($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method5
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Defining new class A
instantiate_new_A:
	subi $sp, $sp, 20
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	li $v0, 9
	li $a0, 12
	syscall
	move $t9, $v0
	la $v0, A_vtable
	sw $v0, 4($t9)
	#Declaring property  var of class Int
	lw $t0, 8($t9)
	li $t0, 0
	sw $t0, 8($t9)
	#Declaring name value
	li $a0, 1
	la $a1, string8
	jal copyString
	sw $v0,  0($t9)
	sw $t0, 8($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	lw $t1, 12($sp)
	lw $t2, 16($sp)
	addi $sp, $sp, 20
	jr $ra
#Declaring new function method5
B_method5:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 12($sp)
	mul $t1, $t0, $t0
	lw $t2, 8($sp)
	sw $t1, 8($sp)
	move $t2, $t1
	#Creating a new instance of the class E
	jal instantiate_new_E
	sw $t0, 4($sp)
	lw $t0, 16($sp)
	#Setting value for instantiate_new_E
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	#We are going to send a param with offset defined
	sw $t2, -316($sp)
	##Calling (newE).set_var(x)
	jal A_set_var
	sw $t0, 16($sp)
	lw $t0, 20($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method5
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Defining new class B
instantiate_new_B:
	subi $sp, $sp, 20
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	li $v0, 9
	li $a0, 12
	syscall
	move $t9, $v0
	la $v0, B_vtable
	sw $v0, 4($t9)
	#Declaring name value
	li $a0, 1
	la $a1, string9
	jal copyString
	sw $v0,  0($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	lw $t1, 12($sp)
	lw $t2, 16($sp)
	addi $sp, $sp, 20
	jr $ra
#Declaring new function method6
C_method6:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 12($sp)
	nor $t1, $t0, $t0
	lw $t2, 8($sp)
	sw $t1, 8($sp)
	move $t2, $t1
	#Creating a new instance of the class A
	jal instantiate_new_A
	sw $t0, 4($sp)
	lw $t0, 16($sp)
	#Setting value for instantiate_new_A
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	#We are going to send a param with offset defined
	sw $t2, -316($sp)
	##Calling (newA).set_var(x)
	lw $s0, 4($t0)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 16($sp)
	lw $t0, 20($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method6
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Declaring new function method5
C_method5:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 12($sp)
	mul $t1, $t0, $t0
	lw $t2, 16($sp)
	mul $t2, $t1, $t0
	sw $t0, 4($sp)
	lw $t0, 8($sp)
	sw $t2, 8($sp)
	move $t0, $t2
	#Creating a new instance of the class E
	jal instantiate_new_E
	sw $t0, 8($sp)
	lw $t0, 20($sp)
	#Setting value for instantiate_new_E
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	#We are going to send a param with offset defined
	sw $t2, -316($sp)
	##Calling (newE).set_var(x)
	jal A_set_var
	sw $t0, 20($sp)
	lw $t0, 24($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method5
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Defining new class C
instantiate_new_C:
	subi $sp, $sp, 20
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	li $v0, 9
	li $a0, 12
	syscall
	move $t9, $v0
	la $v0, C_vtable
	sw $v0, 4($t9)
	#Declaring name value
	li $a0, 1
	la $a1, string10
	jal copyString
	sw $v0,  0($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	lw $t1, 12($sp)
	lw $t2, 16($sp)
	addi $sp, $sp, 20
	jr $ra
#Declaring new function method7
D_method7:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	sw $t0, 8($sp)
	move $t1, $t0
	lw $t2, 12($sp)
	slti $t2, $t1, 0
	bne $t2, 0, TRUE_5
	j FALSE_5
	TRUE_5:
sw $t0, 4($sp)
sw $t0, 8($sp)
sw $t1, 8($sp)
sw $t2, 12($sp)
		sw $t0, 4($sp)
		sw $t0, 8($sp)
		lw $t0, 16($sp)
		nor $t0, $t1, $t1
		#We are going to send a heap
		sw $t9, -320($sp)
		#We are going to send a param with offset defined
		sw $t0, -316($sp)
		lw $s0, 4($t9)
		lw $s0, 36($s0)
		jalr $s0
		sw $t0, 16($sp)
		lw $t0, 20($sp)
		#Setting value for D_method7
		move $t0, $v0
		sw $t1, 8($sp)
		lw $t1, 68($sp)
		sw $t0, 68($sp)
		move $t1, $t0
lw $t0, 4($sp)
lw $t1, 8($sp)
lw $t2, 12($sp)
		j ENDIF_5
	FALSE_5:
sw $t0, 4($sp)
sw $t0, 8($sp)
sw $t1, 8($sp)
sw $t2, 12($sp)
		sw $t0, 4($sp)
		sw $t0, 8($sp)
		lw $t0, 28($sp)
		li $t0, 0
		sw $t0, 28($sp)
		sw $t1, 8($sp)
		lw $t1, 8($sp)
		sw $t2, 12($sp)
		lw $t2, 24($sp)
		xor $t2, $t0, $t1
		slti $t2, $t2, 1
		bne $t2, 0, TRUE_6
		j FALSE_6
		TRUE_6:
sw $t0, 28($sp)
sw $t1, 8($sp)
sw $t2, 24($sp)
			sw $t0, 28($sp)
			lw $t0, 64($sp)
			li $t0, 1
			sw $t0, 64($sp)
lw $t0, 28($sp)
lw $t1, 8($sp)
lw $t2, 24($sp)
			j ENDIF_6
		FALSE_6:
sw $t0, 28($sp)
sw $t1, 8($sp)
sw $t2, 24($sp)
			sw $t0, 28($sp)
			lw $t0, 36($sp)
			li $t0, 1
			sw $t0, 36($sp)
			sw $t2, 24($sp)
			lw $t2, 32($sp)
			xor $t2, $t0, $t1
			slti $t2, $t2, 1
			bne $t2, 0, TRUE_7
			j FALSE_7
			TRUE_7:
sw $t0, 36($sp)
sw $t1, 8($sp)
sw $t2, 32($sp)
				sw $t0, 36($sp)
				lw $t0, 60($sp)
				li $t0, 0
				sw $t0, 60($sp)
lw $t0, 36($sp)
lw $t1, 8($sp)
lw $t2, 32($sp)
				j ENDIF_7
			FALSE_7:
sw $t0, 36($sp)
sw $t1, 8($sp)
sw $t2, 32($sp)
				sw $t0, 36($sp)
				lw $t0, 44($sp)
				li $t0, 2
				sw $t0, 44($sp)
				sw $t2, 32($sp)
				lw $t2, 40($sp)
				xor $t2, $t0, $t1
				slti $t2, $t2, 1
				bne $t2, 0, TRUE_8
				j FALSE_8
				TRUE_8:
sw $t0, 44($sp)
sw $t1, 8($sp)
sw $t2, 40($sp)
					sw $t0, 44($sp)
					lw $t0, 56($sp)
					li $t0, 0
					sw $t0, 56($sp)
lw $t0, 44($sp)
lw $t1, 8($sp)
lw $t2, 40($sp)
					j ENDIF_8
				FALSE_8:
sw $t0, 44($sp)
sw $t1, 8($sp)
sw $t2, 40($sp)
					sw $t0, 44($sp)
					lw $t0, 48($sp)
					subi $t0, $t1, 3
					#We are going to send a heap
					sw $t9, -320($sp)
					#We are going to send a param with offset defined
					sw $t0, -316($sp)
					lw $s0, 4($t9)
					lw $s0, 36($s0)
					jalr $s0
					sw $t0, 48($sp)
					lw $t0, 52($sp)
					#Setting value for D_method7
					move $t0, $v0
					sw $t1, 8($sp)
					lw $t1, 56($sp)
					sw $t0, 56($sp)
					move $t1, $t0
				ENDIF_8:
				lw $t0, 44($sp)
				lw $t1, 8($sp)
				lw $t2, 40($sp)
				sw $t0, 44($sp)
				lw $t0, 56($sp)
				sw $t1, 8($sp)
				lw $t1, 60($sp)
				sw $t0, 60($sp)
				move $t1, $t0
			ENDIF_7:
			lw $t0, 36($sp)
			lw $t1, 8($sp)
			lw $t2, 32($sp)
			sw $t0, 36($sp)
			lw $t0, 60($sp)
			sw $t1, 8($sp)
			lw $t1, 64($sp)
			sw $t0, 64($sp)
			move $t1, $t0
		ENDIF_6:
		lw $t0, 28($sp)
		lw $t1, 8($sp)
		lw $t2, 24($sp)
		sw $t0, 28($sp)
		lw $t0, 64($sp)
		sw $t1, 8($sp)
		lw $t1, 68($sp)
		sw $t0, 68($sp)
		move $t1, $t0
	ENDIF_5:
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method7
	sw $t0, 4($sp)
	sw $t0, 8($sp)
	lw $t0, 68($sp)
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Defining new class D
instantiate_new_D:
	subi $sp, $sp, 20
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	li $v0, 9
	li $a0, 12
	syscall
	move $t9, $v0
	la $v0, D_vtable
	sw $v0, 4($t9)
	#Declaring name value
	li $a0, 1
	la $a1, string11
	jal copyString
	sw $v0,  0($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	lw $t1, 12($sp)
	lw $t2, 16($sp)
	addi $sp, $sp, 20
	jr $ra
#Declaring new function method6
E_method6:
	subi $sp, $sp, 20
	# We are going to store the current state
	sw $t9, 0($sp)
	sw $t0, 4($sp)
	sw $t1, 8($sp)
	sw $t2, 12($sp)
	#Adding space for the parameters + body
	subi $sp, $sp, 300
	sw $ra, 296($sp)
	lw $t9, 0($sp)
	#Entrando a scope de let
	lw $t0, 4($sp)
	lw $t1, 12($sp)
	div $t1, $t0, 8
	lw $t2, 8($sp)
	sw $t1, 8($sp)
	move $t2, $t1
	#Creating a new instance of the class A
	jal instantiate_new_A
	sw $t0, 4($sp)
	lw $t0, 16($sp)
	#Setting value for instantiate_new_A
	move $t0, $v0
	#We are going to pass an instance as a param
	#We are going to send a heap
	sw $t0, -320($sp)
	#We are going to send a param with offset defined
	sw $t2, -316($sp)
	##Calling (newA).set_var(x)
	lw $s0, 4($t0)
	lw $s0, 12($s0)
	jalr $s0
	sw $t0, 16($sp)
	lw $t0, 20($sp)
	#Setting value for A_set_var
	move $t0, $v0
	lw $ra, 296($sp)
	#We are going to assign the response
	#Finalizo la función method6
	move $v0, $t0
	#removing space for the parameters + body
	subi $sp, $sp, -300
	# We are going to restore the current state
	lw $t9, 0($sp)
	lw $t0, 4($sp)
	lw $t1, 8($sp)
	lw $t2, 12($sp)
	subi $sp, $sp, -20
	jr $ra
#Defining new class E
instantiate_new_E:
	subi $sp, $sp, 20
	sw $ra, 0($sp)
	sw $t9, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $t2, 16($sp)
	li $v0, 9
	li $a0, 12
	syscall
	move $t9, $v0
	la $v0, E_vtable
	sw $v0, 4($t9)
	#Declaring name value
	li $a0, 1
	la $a1, string12
	jal copyString
	sw $v0,  0($t9)
	move $v0, $t9
	lw $ra, 0($sp)
	lw $t9, 4($sp)
	lw $t0, 8($sp)
	lw $t1, 12($sp)
	lw $t2, 16($sp)
	addi $sp, $sp, 20
	jr $ra


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

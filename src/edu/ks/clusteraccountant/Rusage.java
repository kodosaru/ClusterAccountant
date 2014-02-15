package edu.ks.clusteraccountant;


/* -*- mode: java; tab-width: 2; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/* From /use/include/linux/resource.h
 struct	{
 struct timeval ru_utime;	 user time used 
 struct timeval ru_stime;	 system time used 
 long	ru_maxrss;		 maximum resident set size  (Stored as float; doc wrong?)
 long	ru_ixrss;		 integral shared memory size 
 long	ru_idrss;		 integral unshared data size 
 long	ru_isrss;		 integral unshared stack size 
 long	ru_minflt;		 page reclaims 
 long	ru_majflt;		 page faults 
 long	ru_nswap;		 swaps 
 long	ru_inblock;		 block input operations (Stored as float; doc wrong?) 
 long	ru_oublock;		 block output operations 
 long	ru_msgsnd;		 messages sent 
 long	ru_msgrcv;		 messages received 
 long	ru_nsignals;		 signals received 
 long	ru_nvcsw;		 voluntary context switches 
 long	ru_nivcsw;		 involuntary " 
 };
 */

class Rusage {
    private long ru_utime;
    private long ru_stime;
    private String ru_maxrss;
    private long ru_ixrss;
    private long ru_ismrss;
    private long ru_idrss;
    private long ru_isrss;
    private long ru_minflt;
    private long ru_majflt;
    private long ru_nswap;
    private String ru_inblock;
    private long ru_oublock;
    private long ru_msgsnd;
    private long ru_msgrcv;
    private long ru_nsignals;
    private long ru_nvcsw;
    private long ru_nivcsw;

    Rusage() {
        this.ru_utime = 0L;
        this.ru_stime = 0L;
        this.ru_maxrss = null;
        this.ru_ixrss = 0L;
        this.ru_ismrss = 0L;
        this.ru_idrss = 0L;
        this.ru_isrss = 0L;
        this.ru_minflt = 0L;
        this.ru_majflt = 0L;
        this.ru_nswap = 0L;
        this.ru_inblock = null;
        this.ru_oublock = 0L;
        this.ru_msgsnd = 0L;
        this.ru_msgrcv = 0L;
        this.ru_nsignals = 0L;
        this.ru_nvcsw = 0L;
        this.ru_nivcsw = 0L;
    }

    public void setAttributes(Long ru_utime, Long ru_stime, String ru_maxrss,
            Long ru_ixrss, Long ru_ismrss, Long ru_idrss, Long ru_isrss,
            Long ru_minflt, Long ru_majflt, Long ru_nswap,
            String ru_inblock, Long ru_oublock, Long ru_msgsnd, Long ru_msgrcv,
            Long ru_nsignals, Long ru_nvcsw, Long ru_nivcsw) {
        this.ru_utime = ru_utime;
        this.ru_stime = ru_stime;
        this.ru_maxrss = ru_maxrss;
        this.ru_ixrss = ru_ixrss;
        this.ru_ismrss = ru_ismrss;
        this.ru_idrss = ru_idrss;
        this.ru_isrss = ru_isrss;
        this.ru_minflt = ru_minflt;
        this.ru_majflt = ru_majflt;
        this.ru_nswap = ru_nswap;
        this.ru_inblock = ru_inblock;
        this.ru_oublock = ru_oublock;
        this.ru_msgsnd = ru_msgsnd;
        this.ru_msgrcv = ru_msgrcv;
        this.ru_nsignals = ru_nsignals;
        this.ru_nvcsw = ru_nvcsw;
        this.ru_nivcsw = ru_nivcsw;
    }

    public void printAttributes() {
        System.out.printf("ru_utime: %d\n", ru_utime);
        System.out.printf("ru_stime: %d\n", ru_stime);
        System.out.printf("ru_maxrss: %s\n", ru_maxrss);
        System.out.printf("ru_ixrss: %d\n", ru_ixrss);
        System.out.printf("ru_ismrss: %d\n", ru_ismrss);
        System.out.printf("ru_idrss: %d\n", ru_idrss);
        System.out.printf("ru_isrss: %d\n", ru_isrss);
        System.out.printf("ru_minflt: %d\n", ru_minflt);
        System.out.printf("ru_majflt: %d\n", ru_majflt);
        System.out.printf("ru_nswap: %d\n", ru_nswap);
        System.out.printf("ru_inblock: %s\n", ru_inblock);
        System.out.printf("ru_oublock: %d\n", ru_oublock);
        System.out.printf("ru_msgsnd: %d\n", ru_msgsnd);
        System.out.printf("ru_msgrcv: %d\n", ru_msgrcv);
        System.out.printf("ru_nsignals: %d\n", ru_nsignals);
        System.out.printf("ru_nvcsw: %d\n", ru_nvcsw);
        System.out.printf("ru_nivcsw: %d\n", ru_nivcsw);
    }
}
